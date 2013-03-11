package shoe.example.metrics;

import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;
import shoe.example.toggles.TrackMetrics;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MetricRecorder_metrics_Test {

  private TrackMetrics trackMetrics;
  private MetricsRecorder recorder;
  private SystemLogger streamSpy;

  @Before
  public void init() {
    trackMetrics = new TrackMetrics();
    recorder = new MetricsRecorder(trackMetrics, "class", "method");
  }

  @Before
  public void redirectOut() {
    streamSpy = mock(SystemLogger.class);

    new NonStrictExpectations(SystemLoggerFactory.class) {{
      SystemLoggerFactory.get((String) any);
      result = streamSpy;
    }};
  }

  @Test
  public void always2LogEntriesWhenMetricsTrackingEnabled() {
    trackMetrics.setEnabled(true);
    recorder.enter();
    recorder.exit(true);

    verifyLogInteractions();
  }

  @Test
  public void always2LogEntriesWhenMetricsTrackingDisabled() {
    trackMetrics.setEnabled(false);
    recorder.enter();
    recorder.exit(true);
    verifyLogInteractions();
  }

  @Test
  public void always2LogEntriesWhenMetricsStartsEnabledAndFinishesDisabled() {
    trackMetrics.setEnabled(true);
    recorder.enter();
    trackMetrics.setEnabled(false);
    recorder.exit(true);
    verifyLogInteractions();
  }

  @Test
  public void always2LogEntriesWhenMetricsStartDisabledAndFinishesEnabled() {
    trackMetrics.setEnabled(false);
    recorder.enter();
    trackMetrics.setEnabled(true);
    recorder.exit(true);
    verifyLogInteractions();
  }

  private void verifyLogInteractions() {
    verify(streamSpy, times(1)).info(anyString(), anyString(), anyString());
    verify(streamSpy, times(1)).info(anyString(), anyString(), anyString(), anyString(), anyInt());
  }

}
