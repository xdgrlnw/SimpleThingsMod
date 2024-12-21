package me.xdgrlnw.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class SimpleLogger {

    private static final Logger LOGGER = Logger.getLogger("Simple Things");
    private static boolean loggingEnabled = true;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

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

    // Включение/выключение логирования
    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
        log("Logging is " + (enabled ? "enabled" : "disabled"));
    }

    // Логирование сообщения
    public static void log(String message) {
        getLogs(Level.INFO, message);
    }

    // Логирование предупреждений
    public static void logWarning(String message) {
        getLogs(Level.WARNING, message);
    }

    // Логирование ошибок
    public static void logError(String message) {
        getLogs(Level.SEVERE, message);
    }

    private static void getLogs(Level level, String message) {
        if (loggingEnabled) {
            LOGGER.log(level, message);
        }
    }

    private static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            String timestamp = DATE_FORMAT.format(new Date(record.getMillis()));
            String threadName = Thread.currentThread().getName();
            String levelName = record.getLevel().getName();
            String source = record.getLoggerName();
            String message = record.getMessage();

            return String.format("[%s] [%s/%s] (%s) %s%n",
                    timestamp, threadName, levelName, source, message);
        }
    }
}
