package me.xdgrlnw.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class SimpleLogger {

    private static final Logger LOGGER = Logger.getLogger("Simple Things");
    private static boolean loggingEnabled = true;

    static {
        setupLogger();
    }

    private static void setupLogger() {
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomFormatter());
        consoleHandler.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.ALL);
    }

    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
        log("Logging is " + (enabled ? "enabled" : "disabled"));
    }

    public static void log(String message) {
        getLogs(Level.INFO, message);
    }

    public static void logWarning(String message) {
        getLogs(Level.WARNING, message);
    }

    public static void logError(String message) {
        getLogs(Level.SEVERE, message);
    }

    private static void getLogs(Level level, String message) {
        if (loggingEnabled) LOGGER.log(level, message);
    }

    private static class CustomFormatter extends Formatter {
        private static final String DATE_FORMAT = "HH:mm:ss";

        @Override
        public String format(LogRecord record) {
            String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date(record.getMillis()));
            String threadName = Thread.currentThread().getName();
            String levelName = record.getLevel().getName();
            String source = record.getLoggerName();
            String message = record.getMessage();

            return String.format("[%s] [%s/%s] (%s) %s%n",
                    timestamp, threadName, levelName, source, message);
        }
    }
}
