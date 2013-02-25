package shoe.example;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SomeComponentImpl implements SomeComponent {
    @Inject
    SomeRepositoryImpl repository;

    @Override
    public void method1() {
        repository.save(this);
    }

    @Override
    public void method2() {
        repository.delete("key");
    }

}
