package shoe.example.schedule;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.MutableDateTime;
import org.springframework.stereotype.Component;


// <codeFragment name = "BusinessDateTimeAdjuster">
@Component
public class BusinessDateTimeAdjuster {
  public void resetToSystemTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  public void setTimeTo(int hour, int minute) {
    DateTimeUtils.setCurrentMillisFixed(todayAt(hour, minute).getMillis());
  }

  DateTime todayAt(int hour, int minute) {
    MutableDateTime dateTime = new MutableDateTime();
    dateTime.setTime(hour, minute, 0, 0);
    return dateTime.toDateTime();
  }
}
// </codeFragment>
