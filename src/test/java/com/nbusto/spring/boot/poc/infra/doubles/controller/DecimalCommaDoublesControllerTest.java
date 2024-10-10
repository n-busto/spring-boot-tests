package com.nbusto.spring.boot.poc.infra.doubles.controller;

import com.nbusto.spring.boot.poc.application.doubles.usecase.CalculateDoubleUseCase;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.infra.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ControllerTest(controllers = DecimalCommaDoublesController.class)
class DecimalCommaDoublesControllerTest extends BaseControllerTest {

  @MockBean
  private CalculateDoubleUseCase useCase;

  @Test
  void when_not_provided_value_then_random_value_is_returned_with_right_format()
    throws Exception {
    // Given
    given(useCase.calculateIfNotPresent(null))
      .willReturn(123.45);

    // Expect
    expectRightValue(get("/doubles/decimal_comma"), 123.45);
  }

  @ParameterizedTest
  @ValueSource(doubles = {0.5, 0.12, -1.10, 12.201, 0.0, -1})
  void given_a_value_then_is_returned_with_right_format(Double value)
    throws Exception {
    // Given
    given(useCase.calculateIfNotPresent(value))
      .willReturn(value);

    // Expect
    expectRightValue(
      get("/doubles/decimal_comma").queryParam("value", String.valueOf(value)),
      value);
  }

  private void expectRightValue(
    MockHttpServletRequestBuilder action,
    double expectedValue) throws Exception {
    performAction(action, 200)
      .andExpect(jsonPath("$.value").exists())
      .andExpect(jsonPath("$.value").isString())
      .andExpect(jsonPath("$.value", is(Double.toString(expectedValue).replace(".", ","))))
      .andExpect(jsonPath("$.value", matchesPattern("^-?[0-9]+,?[0-9]*$")));
  }
}