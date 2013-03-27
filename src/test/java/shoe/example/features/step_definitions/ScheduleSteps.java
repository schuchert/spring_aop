package shoe.example.features.step_definitions;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.joda.time.DateTime;
import shoe.example.schedule.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ScheduleSteps {
  public static final String WORD = "([^ ]+)";
  public static final String NUMBER = "(\\d+)";
  public static final String TIME = NUMBER + ":" + NUMBER;
  ScheduleSystemExample scheduleSystemExample;

  @Before
  public void resetToSystemTime() {
    BusinessDateTimeFactory.restoreSystemTime();
  }

  @After
  public void restoreCurrentTime() {
    BusinessDateTimeFactory.restoreSystemTime();
  }

  @Given("^an empty schedule$")
  public void a_system_with_no_active_work_items() throws Throwable {
    scheduleSystemExample = new ScheduleSystemExample();
  }

  @Given("^a work item named ([^ ]+) scheduled to start at " + TIME + ", last for " + NUMBER + " minutes, and use " + WORD + "$")
  public void a_work_item(String itemName, int startHour, int startMinutes, int durationMinutes, String resourceName) throws Throwable {
    DateTime startDateTime = BusinessDateTimeFactory.todayAt(startHour, startMinutes);
    scheduleSystemExample.scheduleNewWorkItem(itemName, startDateTime, durationMinutes, resourceName);
  }

  @Given("^a first one wins conflict resolution approach$")
  public void a_first_one_wins_confilict_resolution_approach() {
    scheduleSystemExample.setConflictResolutionTo(new FirstOneWins());
  }

  @Given("^the business time is " + TIME + "$")
  public void the_business_time_is(int hour, int minute) {
    setTimeTo(hour, minute);
  }

  @Then("there should be no active items at " + TIME + "$")
  public void there_should_be_no_active_items_at(int hour, int minute) {
    moveTimeForwardTo(hour, minute);
    assertThat(scheduleSystemExample.workItemsIn(Active.class).size(), is(0));
  }

  @Then("^" + WORD + " should be " + WORD + " at " + TIME + "$")
  public void workItem_should_be_state_at(String workItemName, String state, int hour, int minute) {
    moveTimeForwardTo(hour, minute);
    boolean stateMatches = scheduleSystemExample.workItemIs(workItemName, classForStateNamed(state));
    WorkItem item = scheduleSystemExample.workItemNamed(workItemName);
    assertTrue(String.format("Expected state: %s - actual: %s", state, item.getState().getClass().getSimpleName()), stateMatches);
  }

  @Then("^([^ ]+) should be ([^ ]+)$")
  public void should_be_stateX(String workItemName, String state) throws Throwable {
    boolean stateMatches = scheduleSystemExample.workItemIs(workItemName, classForStateNamed(state));
    assertTrue(stateMatches);
  }

  private void moveTimeForwardTo(int hour, int minute) {
    DateTime currentTime = BusinessDateTimeFactory.now();
    DateTime endDateTime = BusinessDateTimeFactory.todayAt(hour, minute);
    while (endDateTime.isAfter(currentTime)) {
      currentTime = currentTime.plusMinutes(1);
      setTimeTo(currentTime.getHourOfDay(), currentTime.getMinuteOfHour());
    }
  }

  private void setTimeTo(int hour, int minute) {
    BusinessDateTimeFactory.setTimeTo(hour, minute);
    scheduleSystemExample.recalculate();
  }

  private Class<? extends WorkItemState> classForStateNamed(String state) {
    if ("active".equals(state)) {
      return Active.class;
    } else if ("pending".equals(state)) {
      return Pending.class;
    } else if ("blocked".equals(state)) {
      return Blocked.class;
    } else if ("completed".equals(state)) {
      return Completed.class;
    } else if ("unshceduled".equals(state)) {
      return Unscheduled.class;
    }

    throw new RuntimeException("Unhandled state: " + state);
  }
}
