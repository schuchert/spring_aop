package shoe.example.schedule;

import org.joda.time.*;

public class DateTimeFactory {
  public static DateTime now() {
    return new DateTime();
  }

  public static void restoreSystemTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  public static DateTime todayAt(int hour, int minute) {
    MutableDateTime dateTime = now().toMutableDateTime();
    dateTime.setTime(hour, minute, 0, 0);
    DateTime result = dateTime.toDateTime();
    return result;
  }

  public static void setTimeTo(int hour, int minute) {
    DateTimeUtils.setCurrentMillisFixed(todayAt(hour, minute).getMillis());
  }
}
