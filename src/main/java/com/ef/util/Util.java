package com.ef.util;

import com.ef.db.dao.LogAccessDao;
import com.ef.model.Argument;
import com.ef.model.RequestLine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mohamedrefaat on 10/14/17.
 */
public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class.getSimpleName());

    // access file date format ex:2017-01-01 00:01:07.328
    private static DateFormat accessFileDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    // argument date format ex:2017-01-01.13:00:00
    private static DateFormat argumentDateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

    // program argument names
    private static final String ARG_START_DATE = "--startDate";
    private static final String ARG_DURATION = "--duration";
    private static final String ARG_THRESHOLD = "--threshold";
    private static final String ARG_FILE_PATH = "--accesslog";

    private static final int DATE_INDEX=0;
    private static final int IP_INDEX=1;
    private static final int REQUEST_INDEX=2;
    private static final int STATUS_INDEX=3;
    private static final int USER_AGENT_INDEX=4;

    // parse a log line
    public static RequestLine parseLine(String line) throws ParseException {

        if (line != null) {
            // Date, IP, Request, Status, User Agent (pipe delimited, open the example file in text editor)
            String[] lineArray = line.split("\\|");
            String dateStr = lineArray[DATE_INDEX];
            String ip = lineArray[IP_INDEX];
            String request = lineArray[REQUEST_INDEX];
            String status = lineArray[STATUS_INDEX];
            String userAgent = lineArray[USER_AGENT_INDEX];

            Date date = accessFileDateFormat.parse(dateStr);
            RequestLine requestLine = new RequestLine(date, ip,request,status,userAgent);

            return requestLine;
        }

        return null;
    }

    // extract argument form string [] to Argument Object
    public static Argument extractArgument(String args[]) throws ParseException {
        Argument argument = new Argument();
        for (String arg : args) {
            if (arg != null && arg.startsWith(ARG_START_DATE)) {
                // getting start date argument
                String startDateStr = arg.split("=")[1];
                argument.setStartDate(argumentDateFormat.parse(startDateStr));
            } else if (arg != null && arg.startsWith(ARG_DURATION)) {
                // getting duration argument
                String durationStr = arg.split("=")[1];
                if (Argument.Duration.daily.toString().equals(durationStr)) {
                    argument.setDuration(Argument.Duration.daily);
                } else if (Argument.Duration.hourly.toString().equals(durationStr)) {
                    argument.setDuration(Argument.Duration.hourly);
                }
            } else if (arg != null && arg.startsWith(ARG_THRESHOLD)) {
                // getting  threshold argument
                String thresholdStr = arg.split("=")[1];
                argument.setThreshold(Integer.parseInt(thresholdStr));
            } else if(arg != null && arg.startsWith(ARG_FILE_PATH)) {
                String filePath = arg.split("=")[1];
                argument.setFilePath(filePath);
            }
        }

        return argument;
    }


    private static Map<String, Integer> ipCounterMap = new HashMap<String, Integer>();
    private static Map<String, Integer> blockedIpMap = new HashMap<String, Integer>();

    public static Map<String, Integer> readLogFileAndFilterBLockedIPs(Argument argument) throws FileNotFoundException, ParseException {

        InputStream is = new FileInputStream(argument.getFilePath());
        Scanner scanner = new Scanner(is);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            RequestLine requestLine = Util.parseLine(line);

            LOGGER.log(Level.FINE, "ip is: " + requestLine.getIp());

            LogAccessDao.loadRequestLogLineToDatabase(requestLine);

            // check the request line for ip occurrence and load it to db if reached the threshold limit
            if(checkRequestLine(requestLine, argument)) {
                LOGGER.log(Level.INFO,"load blocked ip to database: "+requestLine.getIp(),requestLine);
                String comment = "["+requestLine.getIp()+"] reached the ["+argument.getDuration()+"] threshold limit ["+argument.getThreshold()+"] ";
                LogAccessDao.loadBlockedRequestLineToDatabase(requestLine,comment);
            }

        }

        return blockedIpMap;
    }

    // return true if the ip reach the threshold limit
    private static boolean checkRequestLine(RequestLine requestLine, Argument argument) {

        Date date = requestLine.getDate();
        String ip = requestLine.getIp();

        boolean isBlocked = false;

        Calendar comparedDate;
        // if the argument duration is hourly
        if (argument.getDuration() == Argument.Duration.hourly) {
            comparedDate = Calendar.getInstance();
            comparedDate.setTime(argument.getStartDate());
            comparedDate.add(Calendar.HOUR, 1);
        } else {
            // if the argument duration is daily
            comparedDate = Calendar.getInstance();
            comparedDate.setTime(argument.getStartDate());
            comparedDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // check the line for interval from argument
        if (date.after(argument.getStartDate()) && date.before(comparedDate.getTime())) {

            // check if the ip exist or not
            if (ipCounterMap.containsKey(ip)) {
                int newValue = ipCounterMap.get(ip) + 1;
                ipCounterMap.put(ip, newValue);

                // if the new counter value grater than or equal the threshold
                if( newValue >= argument.getThreshold()) {
                    blockedIpMap.put(ip,newValue);
                    isBlocked = true;
                }
            } else {
                // first occurrence for ip
                ipCounterMap.put(ip, 1);
            }
        }
        return isBlocked;
    }


}

