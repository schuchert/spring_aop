package shoe.example.schedule;

import java.util.LinkedList;
import java.util.List;

public class ScheduleSystemExample {
  List<Resource> resources;
  List<WorkItem> workItems;
  private ConflictResolutionApproach conflictResolutionApproach;

  public ScheduleSystemExample() {
    resources = new LinkedList<Resource>();
    workItems = new LinkedList<WorkItem>();
  }

  public Resource findOrAddResourceNamed(String resourceName) {
    Resource resource = resourceNamed(resourceName);

    if (null == resource) {
      resource = new Resource(resourceName);
      resources.add(resource);
    }

    return resource;
  }

  public void add(WorkItem workItem) {
    workItems.add(workItem);
  }

  public void setConflictResolutionTo(ConflictResolutionApproach conflictResolutionApproach) {
    this.conflictResolutionApproach = conflictResolutionApproach;
  }

  public boolean workItemIs(String workItemName, Class<? extends WorkItemState> workItemState) {
    WorkItem item = itemNamed(workItemName);
    return item.stateIs(workItemState);
  }

  private Resource resourceNamed(String resourceName) {
    for (Resource current : resources) {
      if (current.nameEquals(resourceName)) {
        return current;
      }
    }
    return null;
  }

  private WorkItem itemNamed(String workItemName) {
    for (WorkItem current : workItems) {
      if (current.nameEquals(workItemName)) {
        return current;
      }
    }
    throw new WorkItemDoesNotExist(workItemName);
  }

  public List<WorkItem> workItemsIn(Class<? extends WorkItemState> state) {
    LinkedList<WorkItem> itemsInState = new LinkedList<WorkItem>();
    for (WorkItem current : workItems) {
      if (current.stateIs(state)) {
        itemsInState.add(current);
      }
    }
    return itemsInState;
  }

  public void recalculate() {
    for (WorkItem current : workItems) {
      try {
        current.tick();
      } catch (ResourceInUseBy e) {
        conflictResolutionApproach.apply(e.workItem, current);
      }
    }
  }
}
