package shoe.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shoe.example.log.SystemLoggerFactory;

import javax.inject.Inject;
import java.io.PrintStream;
import java.util.logging.Level;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ExerciseAspectTest {
    @Inject
    SomeComponent component;
    @Inject
    SomeRepositoryImpl repository;
    @Inject
    SomeService service;
    @Inject
    SomeRestEndpoints restEndpoint;
    private PrintStream originalOut;
    private PrintStream streamSpy;

    static {
        SystemLoggerFactory.setLevel("org", Level.SEVERE);
    }

    @Before
    public void redirectOut() {
        originalOut = System.out;
        streamSpy = mock(PrintStream.class);
        System.setOut(streamSpy);
    }

    @After
    public void restoreOut() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldProduceOutputForComponent() {
        component.method1();
        component.method2();
        verifyPrintfCalled(4);
    }

    @Test
    public void shouldProductOutputForRepository() {
        repository.save(this);
        verifyPrintfCalled(2);
    }

    @Test
    public void shouldProduceOutputOnRestEndpoint() {
        restEndpoint.method1();
        verifyPrintfCalled(6);
    }

    private void verifyPrintfCalled(int times) {
        verify(streamSpy, times(times)).printf(anyString(), anyString(), anyString());
    }
}
