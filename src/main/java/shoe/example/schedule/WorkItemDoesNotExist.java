package shoe.example.schedule;

public class WorkItemDoesNotExist extends RuntimeException {
  public final String workItemName;

  public WorkItemDoesNotExist(String workItemName) {
    this.workItemName = workItemName;
  }
}
