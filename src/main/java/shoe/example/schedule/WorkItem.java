package shoe.example.schedule;

import org.joda.time.DateTime;

public class WorkItem {
  public final String name;
  public final DateTime start;
  public final int durationMinutes;
  public final Resource resource;
  private WorkItemState state;

  public WorkItem(String itemName, DateTime start, int durationMinutes, Resource resource) {
    this.name = itemName;
    this.start = start;
    this.durationMinutes = durationMinutes;
    this.resource = resource;
    state = new Unscheduled();
  }

  public boolean nameEquals(String workItemName) {
    return name.equals(workItemName);
  }

  public boolean stateIs(Class<?> clazz) {
    return state.getClass() == clazz;
  }

  public void tick() {
    state.tick(this);
  }

  public WorkItemState getState() {
    return state;
  }

  public void setState(WorkItemState state) {
    this.state = state;
  }

  public void tryToStart() {
    try {
      resource.execute(this);
      setState(new Active());
    } catch (ResourceInUseBy e) {
      setState(new Blocked());
    }
  }
}
