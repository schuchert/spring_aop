package shoe.example.schedule;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkItem_stateTransition_fromActive_Should {
  public static final int DURATION = 10;
  public static final int START_HOUR = 10;
  public static final int START_MIN = 10;
  BusinessDateTimeAdjuster adjuster;
  private Resource resource;
  private WorkItem workItem;

  @Before
  public void init() {
    adjuster = new BusinessDateTimeAdjuster();

    resource = new Resource("foo");
    workItem = new WorkItem("Name", BusinessDateTimeFactory.todayAt(START_HOUR, START_MIN), DURATION, resource);

  }

  void setActiveFrom(int hour, int minute) {
    adjuster.setTimeTo(hour, minute);
    workItem.tryToStart();
  }

  @Test
  public void remainActiveWhenStartingOnTimeAndBeforeOriginalEnd() {
    setActiveFrom(START_HOUR, START_MIN);
    adjuster.setTimeTo(START_HOUR, START_MIN + DURATION - 1);
    workItem.tick();
    assertEquals(Active.class, workItem.getState().getClass());
  }

  @Test
  public void shouldBeCompletedWhenStartingOnTimeAndAtOriginalEnd() {
    setActiveFrom(START_HOUR, START_MIN);
    adjuster.setTimeTo(START_HOUR, START_MIN + DURATION);
    workItem.tick();
    assertEquals(Completed.class, workItem.getState().getClass());
  }

  @Test
  public void shouldBeCompleteWhenStartingOnTimeAndAfterOriginalEnd() {
    setActiveFrom(START_HOUR, START_MIN);
    adjuster.setTimeTo(START_HOUR, START_MIN + DURATION + 1);
    workItem.tick();
    assertEquals(Completed.class, workItem.getState().getClass());
  }

  @Test
  public void shouldBeActiveWhenStartingAfterOriginalStartTimeAndDurationHasNotPassed() {
    int actualStartMinute = START_MIN - 1;
    setActiveFrom(START_HOUR, actualStartMinute);
    adjuster.setTimeTo(START_HOUR, actualStartMinute + DURATION - 1);
    workItem.tick();
    assertEquals(Active.class, workItem.getState().getClass());
  }

  @Test
  public void shouldBeCompletedWhenStartingAfterOriginalStartTimeAndDurationHasExactlyPassed() {
    int actualStartMinute = START_MIN - 1;
    setActiveFrom(START_HOUR, actualStartMinute);
    adjuster.setTimeTo(START_HOUR, actualStartMinute + DURATION);
    workItem.tick();
    assertEquals(Completed.class, workItem.getState().getClass());
  }

  @Test
  public void shouldBeCompletedWhenStartingAfterOriginalStartTimeAndDurationHasPassed() {
    int actualStartMinute = START_MIN - 1;
    setActiveFrom(START_HOUR, actualStartMinute);
    adjuster.setTimeTo(START_HOUR, actualStartMinute + DURATION + 1);
    workItem.tick();
    assertEquals(Completed.class, workItem.getState().getClass());
  }
}
