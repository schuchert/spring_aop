package shoe.example.schedule;

public class Resource {
  public final String name;
  private WorkItem current;

  public Resource(String resourceName) {
    this.name = resourceName;
  }

  public void execute(WorkItem workItem) {
    if(current != null) {
      throw new ResourceInUseBy(current);
    }
    current = workItem;
  }

  public void complete(WorkItem workItem) {
    if(current == null) {
      throw new ResourceNotBeingUsedBy(this, workItem);
    }
    current = null;
  }

  public boolean available() {
    return current == null;
  }

  public boolean nameEquals(String resourceName) {
    return name.equals(resourceName);
  }
}
