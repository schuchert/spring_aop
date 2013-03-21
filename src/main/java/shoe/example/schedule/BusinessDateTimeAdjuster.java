package shoe.example.schedule;

import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Component;


@Component
public class BusinessDateTimeAdjuster {
  public void resetToSystemTime() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  public void setTimeTo(int hour, int minute) {
    DateTimeUtils.setCurrentMillisFixed(DateTimeFactory.todayAt(hour, minute).getMillis());
  }
}
