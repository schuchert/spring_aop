package shoe.example.schedule;

public class ResourceNotBeingUsedBy extends RuntimeException {
  private final WorkItem workItem;
  private final Resource resource;

  public ResourceNotBeingUsedBy(Resource resource, WorkItem workItem) {
    this.resource = resource;
    this.workItem = workItem;
  }

  public String toString() {
    return String.format("ResourceNotBeingUsedBy: Name: %s - Resource Name: %s", workItem.name, resource.name);
  }
}
