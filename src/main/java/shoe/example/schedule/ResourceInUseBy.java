package shoe.example.schedule;

public class ResourceInUseBy extends RuntimeException {
  public final WorkItem workItem;

  public ResourceInUseBy(WorkItem workItem) {
    this.workItem = workItem;
  }
}
