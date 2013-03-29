package shoe.example.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import shoe.example.context.SystemApplicationContext;
import shoe.example.server.EmbeddedJetty;

@RunWith(Cucumber.class)
@Cucumber.Options(format = {"pretty", "html:target/cucumber-html-report"}, features = "src/test/resources", glue = "shoe/example/features/step_definitions")
public class LightweightCucumberExecutorTest {
}
