Feature: Test the integration between plugin base elements

  Scenario: Pipeline with ip and print
    Given I have a trigger "ip"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see an ip printed
    And I cleanup the pipeline

  Scenario: Pipeline with echo and print
    Given I have a trigger "echo"
    Given I have an action "print"

    When I start the pipeline
    And I wait 1 seconds

    Then I see something printed
    And I cleanup the pipeline

  Scenario: Pipeline with echo, equals and print (happy path)
    Given I have a trigger "echo"
    Given I load options
      | echo | foo1  |
    Given I have a filter "eq"
    Given I load options
      | compareValue | foo1 |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 1 seconds

    Then I see something printed
    And I cleanup the pipeline

  Scenario: Pipeline with echo, equals and print (sad path)
    Given I have a trigger "echo"
    Given I load options
      | echo | foo2  |
    Given I have a filter "eq"
    Given I load options
      | compareValue | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 1 seconds

    Then I see nothing printed
    And I cleanup the pipeline

  Scenario: Pipeline with page and print
    Given I have a trigger "page"
    Given I load options
      | url | https://duckduckgo.com   |
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see something printed
    And I cleanup the pipeline

  Scenario: Pipeline with page, ip, equals and print
    Given I have a trigger "page"
    Given I load options
      | url | https://duckduckgo.com   |
    Given I have a trigger "ip"
    Given I have a filter "eq"
    Given I load options
      | compareValue | foo |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see nothing printed
    And I cleanup the pipeline

  Scenario: Pipeline with ip, greater and print
    Given I have a trigger "ip"
    Given I have a filter "gt"
    Given I load options
      | compareValue | 0 |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see an ip printed
    And I cleanup the pipeline

  Scenario: Pipeline with echo, less and print
    Given I have a trigger "echo"
    Given I load options
      | echo | 9 |
    Given I have a filter "lt"
    Given I load options
      | compareValue | 99 |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see "9" printed
    And I cleanup the pipeline

  Scenario: Pipeline with ip (load previous state), print
    Given I have a trigger "ip"
    Given I load options
      | stateId | test1 |
    Given I start the pipeline
    And I wait 2 seconds
    Given I cleanup the pipeline
    Given I have a trigger "ip"
    Given I load options
      | stateId | test1 |
    Given I have a transformer "objToStr"
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see nothing printed
    And I cleanup the environment

  Scenario: Pipeline with page (load previous state), print
    Given I have a trigger "page"
    Given I load options
      | stateId | test2 |
      | url     | https://duckduckgo.com |
    Given I start the pipeline
    And I wait 2 seconds
    Given I cleanup the pipeline
    Given I have a trigger "page"
    Given I load options
      | stateId | test2 |
      | url     | https://duckduckgo.com |
    Given I have an action "print"

    When I start the pipeline
    And I wait 2 seconds

    Then I see nothing printed
    And I cleanup the environment