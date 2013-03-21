package shoe.example.schedule;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class Active implements WorkItemState {
  DateTime actualStart;

  public Active() {
    actualStart = DateTimeFactory.now();
  }

  @Override
  public void tick(WorkItem item) {
    DateTime now = DateTimeFactory.now();
    DateTime end = item.start.plusMinutes(item.durationMinutes);

    int minutesPassed = Minutes.minutesBetween(actualStart, now).getMinutes();

    if (minutesPassed >= item.durationMinutes) {
      item.setState(new Completed());
      item.resource.complete(item);
    }
  }
}
