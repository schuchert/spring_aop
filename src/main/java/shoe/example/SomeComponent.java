package shoe.example;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SomeComponent {
    @Inject
    SomeRepository repository;

    public void method1() {
        repository.save(this);
    }

    public void methmuod2() {
        repository.delete("key");
    }
}
