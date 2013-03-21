package shoe.example.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import shoe.example.context.SystemApplicationContext;
import shoe.example.server.EmbeddedJetty;

@Ignore
@RunWith(Cucumber.class)
@Cucumber.Options(format = {"pretty", "html:target/cucumber"}, features = "src/test/resources", glue = "shoe/example/features/step_definitions")
public class FullyIntegratedSystemCucumberExecutor {
  public static final String SPRING_PROFILE = "spring.profiles.active";
  private static EmbeddedJetty jetty;
  private static String originalProfile;

  @BeforeClass
  public static void initJetty() throws Exception {
    originalProfile = System.getProperty(SPRING_PROFILE);
    if (originalProfile == null) {
      originalProfile = "";
    }
    System.setProperty(SPRING_PROFILE, "test");
    jetty = new EmbeddedJetty();
    jetty.start();
    waitForContextInitializationToComplete();
  }

  private static void waitForContextInitializationToComplete() throws InterruptedException {
    int count = 0;
    while (!SystemApplicationContext.isInitialized()) {
      Thread.sleep(100);
      ++count;
      if (count > 100) {
        throw new RuntimeException("SystemApplicationContext not initialized after 10 seconds.");
      }
    }
  }

  @AfterClass
  public static void undoItNow() throws Exception {
    TheDriver.close();
    jetty.stop();
    System.setProperty(SPRING_PROFILE, originalProfile);
  }

  public static String applicationUrl() {
    return jetty.applicationUrl();
  }

}
