package shoe.example.schedule;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkItem_stateTransition_fromUnscheduled_Should {
  BusinessDateTimeAdjuster adjuster;
  private Resource resource;
  private WorkItem workItem;

  @Before
  public void init() {
    adjuster = new BusinessDateTimeAdjuster();

    resource = new Resource("foo");
    workItem = new WorkItem("Name", BusinessDateTimeFactory.todayAt(10, 0), 10, resource);
  }

  @Test
  public void handleTimeBeforeStart() {
    adjuster.setTimeTo(9, 30);
    workItem.tick();
    assertEquals(Pending.class, workItem.getState().getClass());
  }

  @Test
  public void handleTimeAtStart() {
    adjuster.setTimeTo(10, 0);
    workItem.tick();
    assertEquals(Active.class, workItem.getState().getClass());
  }

  @Test
  public void handleTimeAfterScheduledEnd() {
    adjuster.setTimeTo(10, 15);
    workItem.tick();
    assertEquals(NeverExecuted.class, workItem.getState().getClass());
  }
}
