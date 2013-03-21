package shoe.example.schedule;

public class Blocked implements WorkItemState {
  @Override
  public void tick(WorkItem item) {
    if (item.resource.available()) {
      item.tryToStart();
    }
  }
}
