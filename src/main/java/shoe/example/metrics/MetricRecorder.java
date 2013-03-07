package shoe.example.metrics;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import shoe.example.toggles.TrackMetrics;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MetricRecorder {
  public static final String APP_NAME_PROPERTY = "APPLICATION_NAME";
  public static final String PORT_NAME_PROPERTY = "PORT";

  private TimerContext context;
  private long startTime;
  private long stopTime;

  public MetricRecorder(TrackMetrics trackMetrics, String className, String methodName) {
    if (trackMetrics.isEnabled()) {
      MetricName name = new MetricName(group(), className, methodName);
      Timer responses = Metrics.newTimer(name, MILLISECONDS, SECONDS);
      context = responses.time();
    }
  }

  public void enter() {
    startTime = System.currentTimeMillis();
  }

  public void exit() {
    if (context != null) {
      context.stop();
    }
    stopTime = System.currentTimeMillis();
  }

  public long duration() {
    return stopTime - startTime;
  }

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

}
