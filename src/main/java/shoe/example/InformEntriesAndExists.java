package shoe.example;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class InformEntriesAndExists {
    @Around("@target(service)")
    public Object reportServiceEntry(ProceedingJoinPoint jp, Service service) throws Throwable {
        return executeShell(jp, "service");
    }

    @Around("@target(repository)")
    public Object reportResourceExecution(ProceedingJoinPoint jp, Repository repository) throws Throwable {
        return executeShell(jp, "repository");
    }

    private Object executeShell(ProceedingJoinPoint jp, String which) throws Throwable {
        System.out.printf("%s - start: %s\n", which, jp.getSignature());

        Timer responses = Metrics.newTimer(jp.getTarget().getClass(), which, TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
        final TimerContext context = responses.time();

        try {
            Object result = jp.proceed();
            System.out.printf("%s - finish: %s\n", which, jp.getSignature());
            return result;
        } catch (Throwable t) {
            context.stop();
            System.out.printf("%s - failing: %s\n", which, jp.getSignature());
            throw t;
        }
    }
}
