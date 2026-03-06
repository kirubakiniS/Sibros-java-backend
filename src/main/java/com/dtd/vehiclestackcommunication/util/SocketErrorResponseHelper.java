package com.dtd.vehiclestackcommunication.util;

import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SocketErrorResponseHelper {

    private static final Logger logger = Logger.getLogger(SocketErrorResponseHelper.class.getSimpleName());
    private static SocketResponse<String> vciSocketResponse;
    private static SocketResponse<StackResponse> parametersSocketResponse;

    private SocketErrorResponseHelper() {
    }

    public static void setupLogger() {
        try {
            String userName = System.getProperty("user.name");
            String technicianName = Objects.nonNull(userName) && !userName.isEmpty() ? userName : "technician";
            String logFileName = "Vehicle-Stack-Communication-" + technicianName + "-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".log";
            String logFile = "C:\\DTD\\JavaApplication\\stackLog\\" + logFileName;
            Path pathToFile = Paths.get(logFile);
            Files.createDirectories(pathToFile.getParent());
            File file = new File(logFile);
            if (!file.exists() || logger.getHandlers().length == 0) {
                FileHandler fileHandler = new FileHandler(logFile, true);
                logger.addHandler(fileHandler);
                fileHandler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord logRecord) {
                        SimpleDateFormat logTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Calendar cal = new GregorianCalendar();
                        cal.setTimeInMillis(logRecord.getMillis());
                        String level = String.format("%-" + 8 + "s", logRecord.getLevel());
                        return level + logTime.format(cal.getTime()) + " || " + logRecord.getMessage() + "\n";
                    }
                });
            }
        } catch (IOException ex) {
            logger.severe("Error while setting up log file for Stack - " + ex.getMessage());
        }
    }

    public static void logError(String message) {
        if (SystemUtils.IS_OS_WINDOWS) {
            setupLogger();
        }
        logger.severe(message);
    }

    public static void logInfo(String message) {
        if (SystemUtils.IS_OS_WINDOWS) {
            setupLogger();
        }
        logger.info(message);
    }

    public static SocketResponse<String> setConnectivityErrorResponse(String message) {
        logError(message);
        SocketResponse<String> connectivityResponse = new SocketResponse<>();
        connectivityResponse.setMessage(message);
        connectivityResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        connectivityResponse.setError(true);
        return connectivityResponse;
    }

    public static SocketResponse<String> getSocketErrorResponseForVci() {
        return vciSocketResponse;
    }

    public static void setSocketErrorResponseForVci(String message) {
        logError(message);
        vciSocketResponse = new SocketResponse<>();
        vciSocketResponse.setMessage(message);
        vciSocketResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        vciSocketResponse.setError(true);
        vciSocketResponse.setData("");
    }

    public static void clearSocketErrorResponseForVci() {
        vciSocketResponse = null;
    }

    public static SocketResponse<StackResponse> getSocketErrorResponseForParameters() {
        return parametersSocketResponse;
    }

    public static void setSocketErrorResponseForParameters(String message) {
        logError(message);
        parametersSocketResponse = new SocketResponse<>();
        parametersSocketResponse.setMessage(message);
        parametersSocketResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        parametersSocketResponse.setError(true);
        parametersSocketResponse.setData(new StackResponse());
    }

    public static void clearSocketErrorResponseForParameters() {
        parametersSocketResponse = null;
    }
}
