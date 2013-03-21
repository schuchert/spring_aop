package shoe.example.cucumber;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TheDriver {
  static WebDriver driver;

  public static WebDriver getDriver() {
    if(driver == null) {
      driver = new FirefoxDriver();
    }
    return driver;
  }

  static public void close() {
    if(driver != null) {
      driver.close();
    }
    driver = null;
  }
}
