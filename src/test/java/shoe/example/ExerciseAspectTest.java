    package shoe.example;

    import org.junit.After;
    import org.junit.Before;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

    import javax.inject.Inject;
    import java.io.PrintStream;

    import static org.mockito.Mockito.*;

    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration("classpath:applicationContext.xml")
    public class ExerciseAspectTest {
        @Inject
        SomeComponent component;

        @Inject
        SomeRepository repository;

        @Inject
        SomeService service;

        @Inject
        SomeRestEndpoints restEndpoint;

        private PrintStream originalOut;
        private PrintStream streamSpy;
        public static final String ANY_STRING = anyString();

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
        public void shouldProduceNoOutputForComponent() {
            component.method1();
            verifyZeroInteractions(streamSpy);
        }

        @Test
        public void shouldProductOutputForRepository() {
            repository.save(this);
            verify(streamSpy, times(2)).printf(ANY_STRING, ANY_STRING, ANY_STRING);
        }

        @Test
        public void shouldProduceOutputOnRestEndpoint() {
            restEndpoint.method1();
            verify(streamSpy, times(2)).printf(ANY_STRING, ANY_STRING, ANY_STRING);
        }
    }
