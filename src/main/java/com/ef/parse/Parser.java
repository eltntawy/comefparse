package com.ef.parse;

import com.ef.parse.model.Argument;
import com.ef.parse.util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by mohamedrefaat on 10/14/17.
 */
public class Parser {

    static {
        InputStream stream = Parser.class.getClassLoader().
                getResourceAsStream("logger.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Logger LOGGER = Logger.getLogger(Parser.class.getSimpleName());
    private static final String ACCESS_FILE_NAME = "access.log";

    public static void main(String args[]) {
        LOGGER.log(Level.INFO, "<======= Parser Program Started =======>");

        try {
            LOGGER.log(Level.INFO, "Argument Parsing Started");
            Argument argument = Util.extractArgument(args);
            LOGGER.log(Level.INFO, "Argument: "+argument);
            LOGGER.log(Level.INFO, "Argument Parsing Ended");

            LOGGER.log(Level.INFO, "Reading Log File Started");
            Map<String,Integer> blockedIpMap = Util.readLogFileAndFilterBLockedIPs(argument,ACCESS_FILE_NAME);
            LOGGER.log(Level.INFO, "Reading Log File Ended");

            LOGGER.log(Level.INFO, "Blocked IP Map is: "+ blockedIpMap);



        } catch (ParseException | FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error occurred", e);
        }

        LOGGER.log(Level.INFO, "<======= Parser Program Ended =======>");
    }

}
