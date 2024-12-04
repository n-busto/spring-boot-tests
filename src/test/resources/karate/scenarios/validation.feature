Feature: Verify doubles functionallity

  Background:
    Given url baseUrl

  Scenario Outline: Right request body
    Given path '/validation'
    And request <body>
    When method POST
    Then status 200

    Examples:
      | body                                                                                                                                                                |
      | {innerObject: {string: "STR",integer: 6606,doubleValue: -1.0957806279504516E-5},"array": ["NKRYDM"],string: "MWNELCJ",integer: 9063,doubleValue: 8743.242551784579} |
      | {innerObject: {string: "STR",integer: 0,doubleValue: -0.1},"array": ["NKRYDM"],string: "MWNELCJ",integer: 0,doubleValue: 0}                                         |