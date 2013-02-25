package shoe.example.log;

import java.util.logging.Level;

public interface SystemLogger {
    void warn(String message, Object... args);
    void info(String message, Object... args);
    void error(String message, Object... args);
    void error(Exception e);
    void setLevel(Level level);
}
