Feature: Verify doubles functionallity

  Background:
    Given url baseUrl

  Scenario: Double with default formatting
    Given path '/doubles/default'
    When method GET
    Then status 200
    And match response == { double: "#number" }

  Scenario: Specific double with default formatting
    Given path '/doubles/default'
    And param value = 12.5
    When method GET
    Then status 200
    And match response == { double: 12.5 }

  Scenario: Double with default formatting
    Given path '/doubles/decimal_comma'
    When method GET
    Then status 200
    And match response == { double: "#number" }

  Scenario: Specific double with default formatting
    Given path '/doubles/decimal_comma'
    And param value = 12.5
    When method GET
    Then status 200
    And match response == { double: 12.5 }