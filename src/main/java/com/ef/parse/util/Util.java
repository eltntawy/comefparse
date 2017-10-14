package com.ef.parse.util;

import com.ef.parse.Parser;
import com.ef.parse.model.Argument;
import com.ef.parse.model.Argument.Duration;
import com.ef.parse.model.RequestLine;

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

    // parse a log line
    public static RequestLine parseLine(String line) throws ParseException {

        if (line != null) {
            // Date, IP, Request, Status, User Agent (pipe delimited, open the example file in text editor)
            String[] lineArray = line.split("\\|");
            String dateStr = lineArray[0];
            String ip = lineArray[1];

            Date date = accessFileDateFormat.parse(dateStr);
            RequestLine requestLine = new RequestLine(date, ip);

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
                if (Duration.daily.toString().equals(durationStr)) {
                    argument.setDuration(Duration.daily);
                } else if (Duration.hourly.toString().equals(durationStr)) {
                    argument.setDuration(Duration.hourly);
                }
            } else if (arg != null && arg.startsWith(ARG_THRESHOLD)) {
                // getting  threshold argument
                String thresholdStr = arg.split("=")[1];
                argument.setThreshold(Integer.parseInt(thresholdStr));
            }
        }

        return argument;
    }


    private static Map<String, Integer> ipCounterMap = new HashMap<String, Integer>();
    private static Map<String, Integer> blockedIpMap = new HashMap<String, Integer>();

    public static Map<String, Integer> readLogFileAndFilterBLockedIPs(Argument argument, String fileName) throws FileNotFoundException, ParseException {

        ClassLoader classLoader = Parser.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(is);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            RequestLine requestLine = Util.parseLine(line);

            LOGGER.log(Level.FINE, "ip is: " + requestLine.getIp());

            // check the request line for ip occurrence
            checkRequestLine(requestLine, argument);

        }
        return blockedIpMap;
    }

    private static void checkRequestLine(RequestLine requestLine, Argument argument) {

        Date date = requestLine.getDate();
        String ip = requestLine.getIp();

        Calendar comparedDate;
        // if the argument duration is hourly
        if (argument.getDuration() == Duration.hourly) {
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
                }
            } else {
                // first occurrence for ip
                ipCounterMap.put(ip, 1);
            }
        }

    }
}

