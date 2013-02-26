package shoe.example.metrics;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class InformEntriesAndExits {
    public static final String APP_NAME_PROPERTY = "APPLICATION_NAME";
    public static final String PORT_NAME_PROPERTY = "PORT";

    private static String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

    private static String group() {
        String app = System.getProperty(APP_NAME_PROPERTY);
        String port = System.getProperty(PORT_NAME_PROPERTY);
        return String.format("%s.%s.%s", app, hostName(), port);
    }

    @Around("@target(service)")
    public Object reportServiceEntry(ProceedingJoinPoint jp, Service service) throws Throwable {
        return executeShell(jp, "component");
    }

    @Around("@target(component)")
    public Object reportComponentEntry(ProceedingJoinPoint jp, Component component) throws Throwable {
        return executeShell(jp, "component");
    }

    @Around("@target(repository)")
    public Object reportResourceExecution(ProceedingJoinPoint jp, Repository repository) throws Throwable {
        return executeShell(jp, "repository");
    }

    private Object executeShell(ProceedingJoinPoint jp, String which) throws Throwable {
        CorrelationId.enter();

        String className = jp.getSignature().getDeclaringTypeName();
        MetricName name = new MetricName(group(), className, jp.getSignature().getName(), which);

        SystemLogger targetLogger = SystemLoggerFactory.get(className);
        targetLogger.info("start : %s-%s", jp.getSignature().getName(), CorrelationId.get());

        Timer responses = Metrics.newTimer(name, TimeUnit.MILLISECONDS, TimeUnit.SECONDS);
        TimerContext context = responses.time();

        long start = System.currentTimeMillis();
        try {
            Object result = jp.proceed();
            targetLogger.info("finish : %s-%s(%dms)", jp.getSignature().getName(), CorrelationId.get(), System.currentTimeMillis() - start);
            return result;
        } catch (Throwable t) {
            targetLogger.info("failing: %s-%s(%dms)", jp.getSignature().getName(), CorrelationId.get(), System.currentTimeMillis() - start);
            throw t;
        } finally {
            context.stop();
            CorrelationId.exit();
        }

    }
}
