package shoe.example.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;

import javax.inject.Inject;

@Aspect
@Component
public class InformEntriesAndExits {
  public static final String APP_NAME_PROPERTY = "APPLICATION_NAME";
  public static final String PORT_NAME_PROPERTY = "PORT";
  @Inject
  MetricRecorder metricRecorder;

  @Around("@target(service) && within(shoe.example.service..*)")
  public Object reportServiceEntry(ProceedingJoinPoint jp, Service service) throws Throwable {
    return executeShell(jp);
  }

  @Around("@target(component) && within(shoe.example.component..*)")
  public Object reportComponentEntry(ProceedingJoinPoint jp, Component component) throws Throwable {
    return executeShell(jp);
  }

  @Around("@target(repository) && within(shoe.example.repo..*)")
  public Object reportResourceExecution(ProceedingJoinPoint jp, Repository repository) throws Throwable {
    return executeShell(jp);
  }

  private Object executeShell(ProceedingJoinPoint jp) throws Throwable {
    CorrelationId.enter();

    String className = jp.getSignature().getDeclaringTypeName();
    String methodName = jp.getSignature().getName();

    SystemLogger targetLogger = SystemLoggerFactory.get(className);
    targetLogger.info("start : %s-%s", methodName, CorrelationId.get());

    long start = System.currentTimeMillis();
    boolean success = false;
    try {
      metricRecorder.enter(className, methodName);
      Object result = jp.proceed();
      success = true;
      return result;
    } finally {
      metricRecorder.exit(className, methodName);
      String result = success ? "finish" : "failure";
      targetLogger.info("%7s: %s-%s(%dms)", result, methodName, CorrelationId.get(), System.currentTimeMillis() - start);
      CorrelationId.exit();
    }
  }
}
