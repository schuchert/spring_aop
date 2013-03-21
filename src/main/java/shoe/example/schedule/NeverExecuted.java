package shoe.example.schedule;

public class NeverExecuted implements WorkItemState {
  @Override
  public void tick(WorkItem item) {
  }
}
