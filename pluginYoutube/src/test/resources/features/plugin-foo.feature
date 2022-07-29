Feature: Test the integration between plugin youtube elements

  Scenario: video trigger (3 new) and print
    Given I have the trigger "video" with 3 new videos
    Given I load options
      | channelId | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see something printed
    And I cleanup the pipeline

  Scenario: video trigger (0 new) and print
    Given I have the trigger "video" with 0 new videos
    Given I load options
      | channelId | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see nothing printed
    And I cleanup the pipeline

  Scenario: video trigger (3 new and then same, state) and print
    Given I have the trigger "video" with 3 new videos
    Given I load options
      | stateId | test1 |
      | channelId | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"
    Given I start the pipeline
    And I wait 2 seconds
    Given I cleanup the pipeline

    Given I have the trigger "video" with the same new videos
    Given I load options
      | stateId | test1 |
      | channelId | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see nothing printed
    And I cleanup the environment

  Scenario: video trigger (3 new and then same) and print
    Given I have the trigger "video" with 3 new videos
    Given I load options
      | channelId | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"
    Given I start the pipeline
    And I wait 2 seconds
    Given I cleanup the pipeline

    Given I have the trigger "video" with the same new videos
    Given I load options
      | channelId | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 3 seconds

    Then I see something printed
    And I cleanup the environment
