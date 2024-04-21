package org.example.logger;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.constant.AppConstants.LOGS_DIRECTORY;

public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());

    static {
        try {
            LOGGER.setUseParentHandlers(false);

            if (Files.exists(Path.of(LOGS_DIRECTORY))) {
                final FileHandler fileHandler =
                        new FileHandler("%s/assignment-app-%s.log"
                                .formatted(LOGS_DIRECTORY, LocalDateTime.now()), true);
                fileHandler.setFormatter(new PrettyFormatter());
                LOGGER.addHandler(fileHandler);
            }

            final ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new PrettyFormatter());

            LOGGER.addHandler(handler);
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
