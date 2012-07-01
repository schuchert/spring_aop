package shoe.example;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SomeService {
    @Inject
    SomeComponent component;

    public void method1() {
        component.method1();
    }

    public void methodThrowingException() {
        throw new RuntimeException();
    }
}
