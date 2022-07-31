package md.dev.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;

public class LoggerHandler {
    private static Level previousLogLevel;
    private static final Logger ROOT_LOG = (ch.qos.logback.classic.Logger)
        org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);


    public static void disableLog() {
        previousLogLevel = ROOT_LOG.getLevel();
        ROOT_LOG.setLevel(Level.OFF);
    }


    public static void enableLog() {
        ROOT_LOG.setLevel(previousLogLevel);
    }


    public static void setLog(Level level) {
        previousLogLevel = ROOT_LOG.getLevel();
        ROOT_LOG.setLevel(level);
    }
}
