Feature: Handling Scheduling Conflicts
  As an operator I want to make sure feature conflicts are managed by an appropriate policy.

  Background:
    Given an empty schedule
    And a work item named Megatron_Torso scheduled to start at 10:00, last for 15 minutes, and use 3d_printer_1
    And a work item named Megatron_Head scheduled to start at 10:10, last for 5 minutes, and use 3d_printer_1
    And a first one wins conflict resolution approach
    And the business time is 9:59

  Scenario: Nothing going on
    Then there should be no active items at 9:59

  Scenario: One item active
    Then Megatron_Torso should be active at 10:01

  Scenario: Conflict Handled
    Then Megatron_Torso should be active at 10:10
    And Megatron_Head should be blocked

  Scenario: Delayed Start
    Then Megatron_Torso should be completed at 10:16
    And Megatron_Head should be active

  Scenario: Delayed work item finishes late
    Then Megatron_Head should be completed at 10:21