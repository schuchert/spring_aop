package shoe.example.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class SystemLoggerFactory {
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    public static SystemLogger get(String context) {
        Logger logger = LoggerFactory.getLogger(context);
        return new SystemLoggerImpl(logger);
    }

    public static SystemLogger get(Class<?> clazz) {
        return get(clazz.getName());
    }

    public static void setLevel(String name, Level level) {
        get(name).setLevel(level);
    }

    public static void setLevel(Class<?> clazz, Level level) {
        setLevel(clazz.getName(), level);
    }
}
