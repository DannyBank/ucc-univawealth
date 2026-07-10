package com.dbank.uccunivawealth.service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LoggerService {

    private static final String LOG_FOLDER = System.getProperty("user.home")
            + File.separator + "UccUnivaWealth" + File.separator
            + "logs";

    private LoggerService() {
    }

    /**
     * Logs an exception and displays a generic error message.
     *
     * @param ex Exception to log
     */
    public static void log(Exception ex) {

        try {

            // Create logs directory if it doesn't exist
            Files.createDirectories(Path.of(LOG_FOLDER));

            String fileName = "error-" + LocalDate.now() + ".txt";
            Path logFile = Path.of(LOG_FOLDER, fileName);

            StringBuilder log = new StringBuilder();

            log.append("====================================================\n");
            log.append("Date/Time : ")
                    .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .append("\n");

            if (ex.getStackTrace().length > 0) {

                StackTraceElement element = ex.getStackTrace()[0];

                log.append("Class     : ")
                        .append(element.getClassName())
                        .append("\n");

                log.append("Method    : ")
                        .append(element.getMethodName())
                        .append("\n");

                log.append("Line      : ")
                        .append(element.getLineNumber())
                        .append("\n");
            }

            log.append("Exception : ")
                    .append(ex.getClass().getName())
                    .append("\n");

            log.append("Message   : ")
                    .append(ex.getMessage())
                    .append("\n\n");

            log.append("Stack Trace:\n");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            log.append(sw);
            log.append("\n\n");

            Files.writeString(
                    logFile,
                    log.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }

        showGenericAlert();
    }

    public static void logE(Exception ex) {

        try {

            // Create logs directory if it doesn't exist
            Files.createDirectories(Path.of(LOG_FOLDER));

            String fileName = "error-" + LocalDate.now() + ".txt";
            Path logFile = Path.of(LOG_FOLDER, fileName);

            StringBuilder log = new StringBuilder();

            log.append("====================================================\n");
            log.append("Date/Time : ")
                    .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .append("\n");

            if (ex.getStackTrace().length > 0) {

                StackTraceElement element = ex.getStackTrace()[0];

                log.append("Class     : ")
                        .append(element.getClassName())
                        .append("\n");

                log.append("Method    : ")
                        .append(element.getMethodName())
                        .append("\n");

                log.append("Line      : ")
                        .append(element.getLineNumber())
                        .append("\n");
            }

            log.append("Exception : ")
                    .append(ex.getClass().getName())
                    .append("\n");

            log.append("Message   : ")
                    .append(ex.getMessage())
                    .append("\n\n");

            log.append("Stack Trace:\n");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            log.append(sw);
            log.append("\n\n");

            Files.writeString(
                    logFile,
                    log.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    /**
     * Displays a generic error message.
     */
    private static void showGenericAlert() {

        Runnable task = () -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred, please try again.");
            alert.showAndWait();
        };

        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }
}
