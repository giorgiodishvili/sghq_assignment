package org.example.logger;


import java.time.LocalDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/app-%s.log".formatted(LocalDateTime.now()), true);
            fileHandler.setFormatter(new PrettyFormatter());
            final ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new PrettyFormatter());
            LOGGER.addHandler(handler);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.INFO);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occur in setting up the logger.", e);
        }
    }

    public static void logInfo(String message) {
        LOGGER.info(message);
    }

    public static void logFine(String message) {
        LOGGER.fine(message);
    }

    public static void logError(String message, Throwable thrown) {
        LOGGER.log(Level.SEVERE, message, thrown);
    }
}
