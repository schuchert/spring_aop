package shoe.example.logging;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JdkLoggingExample {
  @Test
  public void unnecessaryStringConcat() {
    Logger logger = Logger.getLogger(getClass().getName());
    String message =
      String.format("%s-%s-%s", "part1", "part2", "part3");
    logger.log(Level.INFO, message);
  }

  @Test
  public void betterButDryViolation() {
    Logger logger = Logger.getLogger(getClass().getName());
    if (logger.isLoggable(Level.INFO)) {
      String message =
        String.format("%s-%s-%s", "part1", "part2", "part3");
      logger.log(Level.INFO, message);
    }
  }
}
