package shoe.example.schedule;

import org.joda.time.DateTime;

public class Blocked implements WorkItemState {
  @Override
  public void tick(WorkItem item) {
    DateTime now = DateTimeFactory.now();

    if(item.resource.available()) {
      item.tryToStart();
    }
  }
}
