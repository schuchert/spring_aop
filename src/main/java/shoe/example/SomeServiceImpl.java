package shoe.example;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SomeServiceImpl implements SomeService {
    @Inject
    SomeComponent component;

    @Override
    public void method1() {
        component.method1();
    }

    @Override
    public void methodThrowingException() {
        throw new RuntimeException();
    }
}
