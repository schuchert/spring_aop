package shoe.example.log;

import org.slf4j.Logger;

import java.util.logging.Level;

public class SystemLoggerImpl implements SystemLogger {
    private final Logger logger;

    public SystemLoggerImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warn(String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(String.format(message, args));
        }
    }

    @Override
    public void info(String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format(message, args));
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(String.format(message, args));
        }
    }

    @Override
    public void error(Exception e) {
        error(e.toString());
    }

    @Override
    public void setLevel(Level level) {
        ch.qos.logback.classic.Level newLevel;

        if (level.equals(Level.INFO)) {
            newLevel = ch.qos.logback.classic.Level.INFO;
        } else if (level.equals(Level.WARNING)) {
            newLevel = ch.qos.logback.classic.Level.WARN;
        } else if (level.equals(Level.SEVERE)) {
            newLevel = ch.qos.logback.classic.Level.ERROR;
        } else if (level.equals(Level.FINEST)) {
            newLevel = ch.qos.logback.classic.Level.ALL;
        } else {
            throw new RuntimeException(String.format(
                    "Level not configured for use: %s",
                    level.getName()));
        }

        ((ch.qos.logback.classic.Logger) logger).setLevel(newLevel);
    }
}
