package shoe.example.features.step_definitions;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.joda.time.DateTime;
import shoe.example.schedule.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ScheduleSteps {
  ScheduleSystemExample scheduleSystemExample;

  @Before
  public void resetToSystemTime() {
    DateTimeFactory.restoreSystemTime();
  }

  @After
  public void restoreCurrentTime() {
    DateTimeFactory.restoreSystemTime();
  }

  @Given("^a system with no active work items$")
  public void a_system_with_no_active_work_items() throws Throwable {
    scheduleSystemExample = new ScheduleSystemExample();
  }

  @Given("^a work item named ([^ ]+) scheduled to start at (\\d+):(\\d+), last for (\\d+) minutes, and use ([^ ]+)$")
  public void a_work_item(String itemName, int startHour, int startMinutes, int durationMinutes, String resourceName) throws Throwable {
    Resource r = scheduleSystemExample.findOrAddResourceNamed(resourceName);
    DateTime startDateTime = DateTimeFactory.todayAt(startHour, startMinutes);
    WorkItem workItem = new WorkItem(itemName, startDateTime, durationMinutes, r);
    scheduleSystemExample.add(workItem);
  }

  @Given("^a first one wins conflict resolution approach$")
  public void a_first_one_wins_confilict_resolution_approach() {
    scheduleSystemExample.setConflictResolutionTo(new FirstOneWins());
  }

  @When("^now is (\\d+):(\\d+)$")
  public void now_is(int hour, int minute) {
    DateTimeFactory.setTimeTo(hour, minute);
    scheduleSystemExample.recalculate();
  }

  @Then("^there should be no active items$")
  public void there_should_be_no_active_items() throws Throwable {
    assertThat(scheduleSystemExample.workItemsIn(Active.class).size(), is(new Integer(0)));
  }

  @Then("^([^ ]+) should be ([^ ]+)$")
  public void should_be_stateX(String workItemName, String state) throws Throwable {

    Class<? extends WorkItemState> clazz = null;
    if ("active".equals(state)) {
      clazz = Active.class;
    } else if ("pending".equals(state)) {
      clazz = Pending.class;
    } else if ("blocked".equals(state)) {
      clazz = Blocked.class;
    } else if ("completed".equals(state)) {
      clazz = Completed.class;
    } else if ("unshceduled".equals(state)) {
      clazz = Unscheduled.class;
    } else {
      fail("Unhandled state: " + state);
    }

    boolean stateMatches = scheduleSystemExample.workItemIs(workItemName, clazz);
    assertTrue(stateMatches);
  }

}
