package shoe.example.schedule;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorkItem_stateTransition_fromPending_Should {
  BusinessDateTimeAdjuster adjuster;
  private Resource resource;
  private WorkItem workItem;

  @Before
  public void init() {
    adjuster = new BusinessDateTimeAdjuster();

    resource = new Resource("foo");
    workItem = new WorkItem("Name", BusinessDateTimeFactory.todayAt(10, 0), 10, resource);
    workItem.setState(new Pending());
  }

  @Test
  public void stayInPendingBeforeTime() {
    adjuster.setTimeTo(9,59);
    workItem.tick();
    assertEquals(Pending.class, workItem.getState().getClass());
  }

  @Test
  public void activateAtTime() {
    adjuster.setTimeTo(10,0);
    workItem.tick();
    assertEquals(Active.class, workItem.getState().getClass());
  }

  @Test
  public void activateAfterTimeBeforeEnd() {
    adjuster.setTimeTo(10,9);
    workItem.tick();
    assertEquals(Active.class, workItem.getState().getClass());
  }

  @Test
  public void activeAfterTimeAfterEnd() {
    adjuster.setTimeTo(10,11);
    workItem.tick();
    assertEquals(Active.class, workItem.getState().getClass());
  }
}
