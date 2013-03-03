package shoe.example.logging;

import org.junit.Test;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;

public class WrappedLoggingExample {
  @Test
  public void cannotDoItBadly() {
    SystemLogger logger = SystemLoggerFactory.get(getClass());
    logger.info("%s-%s-%s", "part1", "part2", "part3");
  }
}
