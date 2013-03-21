package shoe.example.schedule;

public interface WorkItemState {
  void tick(WorkItem item);
}
