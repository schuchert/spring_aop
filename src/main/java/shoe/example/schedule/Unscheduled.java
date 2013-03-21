package shoe.example.schedule;

import org.joda.time.DateTime;


public class Unscheduled implements WorkItemState {
  @Override
  public void tick(WorkItem item) {
    DateTime now = BusinessDateTimeFactory.now();
    DateTime end = item.start.plusMinutes(item.durationMinutes);

    if (now.equals(item.start) || now.isAfter(item.start) && end.isAfter(now)) {
      item.tryToStart();
    } else if (end.isBefore(now)) {
      item.setState(new NeverExecuted());
    } else {
      item.setState(new Pending());
    }
  }
}
