Feature: Verify healthcheck on application

  Background:
    Given url baseUrl

  Scenario: Happy path
    Given path '/healthcheck'
    When method GET
    Then status 200
    And match response == { status: "UP" }