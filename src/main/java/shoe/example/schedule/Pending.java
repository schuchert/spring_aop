package shoe.example.schedule;

import org.joda.time.DateTime;

public class Pending implements WorkItemState {
  @Override
  public void tick(WorkItem item) {
    DateTime now = BusinessDateTimeFactory.now();

    if (now.equals(item.start) || now.isAfter(item.start)) {
      item.tryToStart();
    }
  }
}
