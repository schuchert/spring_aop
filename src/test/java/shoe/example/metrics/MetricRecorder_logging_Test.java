package shoe.example.metrics;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.core.TimerContext;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;
import shoe.example.toggles.TrackMetrics;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class MetricRecorder_logging_Test {

  private TrackMetrics trackMetrics;
  private MetricsRecorder recorder;
  private TimerContext context;
  private Timer timer;

  @Before
  public void init() {
    trackMetrics = new TrackMetrics();
    recorder = new MetricsRecorder(trackMetrics, "class", "method");
  }

  @Before
  public void redirectOut() {

    new NonStrictExpectations(SystemLoggerFactory.class) {{
      SystemLoggerFactory.get((String) any);
      result = mock(SystemLogger.class);
    }};
  }

  @Before
  public void pwnYammer() {
    timer = mock(Timer.class);
    context = mock(TimerContext.class);

    new NonStrictExpectations(Metrics.class) {{
      Metrics.newTimer((MetricName) any, (TimeUnit) any, (TimeUnit) any);
      result = timer;
    }};

    when(timer.time()).thenReturn(context);
  }

  @Test
  public void tracksMetricsIfEnabledAtStart() {
    trackMetrics.setEnabled(true);
    recorder.enter();
    recorder.exit(true);

    verify(timer, times(1)).time();
    verify(context, times(1)).stop();
  }

  @Test
  public void doesNotTrackMetricsIfNotEnabledAtStart() {
    trackMetrics.setEnabled(false);
    recorder.enter();
    recorder.exit(true);

    verify(timer, times(0)).time();
    verify(context, times(0)).stop();
  }

  @Test
  public void tracksMetricsIfEnabledAtStartButDisabledAtEnd() {
    trackMetrics.setEnabled(true);
    recorder.enter();
    trackMetrics.setEnabled(false);
    recorder.exit(true);

    verify(timer, times(1)).time();
    verify(context, times(1)).stop();
  }

  @Test
  public void doesNotTrackMetricsIfDisabledAtStartAndEnabledAtEnd() {
    trackMetrics.setEnabled(false);
    recorder.enter();
    trackMetrics.setEnabled(true);
    recorder.exit(true);

    verify(timer, times(0)).time();
    verify(context, times(0)).stop();
  }
}
