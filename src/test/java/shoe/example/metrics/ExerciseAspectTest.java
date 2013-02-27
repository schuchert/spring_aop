package shoe.example.metrics;

import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shoe.example.component.SomeComponent;
import shoe.example.endpoint.SomeRestEndpoints;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;
import shoe.example.repo.SomeRepositoryImpl;
import shoe.example.service.SomeService;

import javax.inject.Inject;
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
    private SystemLogger streamSpy;

    static {
        SystemLoggerFactory.setLevel("org", Level.SEVERE);
    }

    @Before
    public void redirectOut() {
        streamSpy = mock(SystemLogger.class);

        new NonStrictExpectations(SystemLoggerFactory.class) {{
            SystemLoggerFactory.get((String) any);
            result = streamSpy;
        }};
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
        verifyPrintfCalled(1);
    }

    @Test
    public void shouldProduceOutputOnRestEndpoint() {
        restEndpoint.method1();
        verifyPrintfCalled(4);
    }

    private void verifyPrintfCalled(int times) {
        verify(streamSpy, times(times)).info(anyString(), anyString(), anyString());
    }
}
