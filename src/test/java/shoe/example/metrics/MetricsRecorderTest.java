package shoe.example.metrics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import shoe.example.log.SystemLogger;
import shoe.example.log.SystemLoggerFactory;
import shoe.example.toggles.TrackMetrics;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore( {"javax.management.*"})
@PrepareForTest({MetricsRecorder.class, SystemLoggerFactory.class})
public class MetricsRecorderTest {
  @Mock
  SystemLogger logger;

  @Mock
  CorrelationId id;

  @Before
  public void init() throws Exception {
    replaceCorrelationId();
    replaceLogger();
    executeMetricsLogger();
  }

  public void replaceCorrelationId() throws Exception {
    PowerMockito.whenNew(CorrelationId.class).withAnyArguments().thenReturn(id);
    when(id.get()).thenReturn("1.1");
  }

  public void replaceLogger() {
    PowerMockito.mockStatic(SystemLoggerFactory.class);
    when(SystemLoggerFactory.get(anyString())).thenReturn(logger);
  }

  private void executeMetricsLogger() {
    MetricsRecorder recorder = new MetricsRecorder(new TrackMetrics(), "className", "methodName");
    recorder.enter();
    recorder.exit(true);
  }

  @Test
  public void usesCorrelationIdCorrectly() {
    verify(id, times(1)).enter();
    verify(id, times(1)).exit();
  }

  @Test
  public void callsLoggerTwice() {
    verify(logger, times(1)).info(anyString(), anyObject(), anyObject());
    verify(logger, times(1)).info(anyString(), anyObject(), anyObject(), anyObject(), anyObject());
  }
}
