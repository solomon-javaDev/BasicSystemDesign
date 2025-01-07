package main.java.security;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE = "system.log";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public enum LogLevel {
        INFO, WARNING, ERROR
    }

    private LogLevel currentLevel = LogLevel.INFO;

    public void setLogLevel(LogLevel level) {
        this.currentLevel = level;
    }

    private void logToFile(String message) {
        String timestamp = dateFormat.format(new Date());
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(timestamp + " " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileEncryptor.encryptFile(LOG_FILE, LOG_FILE + ".enc");
            new File(LOG_FILE).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logInfo(String message) {
        if (currentLevel.ordinal() <= LogLevel.INFO.ordinal()) {
            logToFile("INFO: " + message);
        }
    }

    public void logWarning(String message) {
        if (currentLevel.ordinal() <= LogLevel.WARNING.ordinal()) {
            logToFile("WARNING: " + message);
        }
    }

    public void logError(String message) {
        if (currentLevel.ordinal() <= LogLevel.ERROR.ordinal()) {
            logToFile("ERROR: " + message);
        }
    }
}
