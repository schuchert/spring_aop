package shoe.example.schedule;

import org.joda.time.DateTime;


public class Unscheduled implements WorkItemState {
  @Override
  public void tick(WorkItem item) {
    DateTime now = DateTimeFactory.now();
    DateTime end = item.start.plusMinutes(item.durationMinutes);

    if (now.equals(item.start) || now.isAfter(item.start) && end.isAfter(now)) {
      item.tryToStart();
    } else if (end.isBefore(now)) {
      item.setState(new NeverExecuted());
    } else {
      item.setState(new Pending());
    }
  }

  private boolean wholeScheduleInFuture(WorkItem item, DateTime now) {
    return now.isAfter(item.start);
  }

  private boolean wholeScheudleInPast(DateTime now, DateTime end) {
    return end.isBefore(now);
  }
}
