package shoe.example.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import shoe.example.context.SystemApplicationContext;
import shoe.example.server.EmbeddedJetty;

@RunWith(Cucumber.class)
@Cucumber.Options(format = {"pretty", "html:target/cucumber"}, features = "src/test/resources", glue = "shoe/example/features/step_definitions")
public class LightweightCucumberExecutor {
  public static final String SPRING_PROFILE = "spring.profiles.active";
  private static String originalProfile;

  @BeforeClass
  public static void initJetty() throws Exception {
    originalProfile = System.getProperty(SPRING_PROFILE);
    if (originalProfile == null) {
      originalProfile = "";
    }
    System.setProperty(SPRING_PROFILE, "test");
//    ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/Users/schuchert/src/spring_aop/src/main/webapp/applicationContext.xml");
  }

  @AfterClass
  public static void undoItNow() throws Exception {
    System.setProperty(SPRING_PROFILE, originalProfile);
  }
}
