package shoe.example.metrics;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import org.springframework.stereotype.Component;
import shoe.example.toggles.TrackMetrics;

import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class MetricRecorder {
  public static final String APP_NAME_PROPERTY = "APPLICATION_NAME";
  public static final String PORT_NAME_PROPERTY = "PORT";
  private static ThreadLocal<TimerContext> context = new ThreadLocal<TimerContext>();
  @Inject
  TrackMetrics trackMetrics;

  private String hostName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      return "UnknownHost";
    }
  }

  private String group() {
    String app = System.getProperty(APP_NAME_PROPERTY);
    String port = System.getProperty(PORT_NAME_PROPERTY);
    return String.format("%s.%s.%s", app, hostName(), port);
  }

  private MetricName metricName(String className, String methodName) {
    return new MetricName(group(), className, methodName);
  }

  public void enter(String className, String methodName) {
    if (trackMetrics.isEnabled()) {
      MetricName name = metricName(className, methodName);
      Timer responses = Metrics.newTimer(name, MILLISECONDS, SECONDS);
      context.set(responses.time());
    }
  }

  public void exit(String className, String methodName) {
    if (context.get() != null) {
      context.get().stop();
    }
    context.remove();
  }
}
