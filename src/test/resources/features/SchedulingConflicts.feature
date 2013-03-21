Feature: Handling Scheduling Conflicts
  As an operator I want to make sure feature conflicts are managed by an appropriate policy.

  Background:
    Given a system with no active work items
    And a work item named I1 scheduled to start at 10:00, last for 15 minutes, and use R1
    And a work item named I2 scheduled to start at 10:10, last for 5 minutes, and use R1
    And a first one wins conflict resolution approach

  Scenario: Nothing going on
    When now is 9:59
    Then there should be no active items

  Scenario: One item active
    When now is 10:01
    Then I1 should be active

  Scenario: Conflict Resolved
    When now is 10:10
    Then I1 should be active
    And I2 should be blocked

  Scenario: Delayed Start
    Given now is 10:00
    When now is 10:16
    Then I1 should be completed
    And I2 should be active

  Scenario: Delayed finished
    Given now is 10:00
    And now is 10:15
    When now is 10:21
    Then I2 should be completed