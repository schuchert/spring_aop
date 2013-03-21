package shoe.example.schedule;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class BusinessDateTimeAdjusterShould {
  @Before
  public void setTheTime() {
    new BusinessDateTimeAdjuster().setTimeTo(10, 17);
  }

  @After
  public void resetTheTime() {
    new BusinessDateTimeAdjuster().resetToSystemTime();
  }

  @Test
  public void beResettable() throws Exception {
    DateTime before = new DateTime();
    new BusinessDateTimeAdjuster().resetToSystemTime();
    DateTime after = new DateTime();
    if (after.equals(before)) {
      Thread.sleep(10);
      after = new DateTime();
    }
    assertNotEquals(after, before);
  }

  @Test
  public void beAbleToSetCurrentDateTimeDuringCallToNew() {
    DateTime dateTime = new DateTime();
    validateTime(dateTime);
  }

  @Test
  public void beAbleToSetCurrentDateTimeWithinFactory() {
    DateTime dateTime = DateTimeFactory.now();
    validateTime(dateTime);
  }

  private void validateTime(DateTime dateTime) {
    assertThat(dateTime.toLocalTime().getHourOfDay(), is(10));
    assertThat(dateTime.toLocalTime().getMinuteOfHour(), is(17));
    assertThat(dateTime.getSecondOfMinute(), is(0));
    assertThat(dateTime.getMillisOfSecond(), is(0));
  }
}
