package com.nbusto.spring.boot.poc.infra.doubles.controller;

import com.nbusto.spring.boot.poc.application.doubles.usecase.CalculateDoubleUseCase;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = DecimalCommaDoublesController.class)
class DecimalCommaDoublesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ResultMatcher openApiValidator;

  @MockBean
  private CalculateDoubleUseCase useCase;

  @Test
  void when_not_provided_value_then_random_value_is_returned_with_right_format()
    throws Exception {
    // Given
    given(useCase.calculateIfNotPresent(null))
      .willReturn(123.45);

    // Expect
    mockMvc.perform(get("/doubles/decimal_comma"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(openApiValidator)
      .andExpect(jsonPath("$.value").exists())
      .andExpect(jsonPath("$.value").isString())
      .andExpect(jsonPath("$.value", is(Double.toString(123.45).replace(".", ","))))
      .andExpect(jsonPath("$.value", matchesPattern("^-?[0-9]+,?[0-9]*$")));
  }

  @ParameterizedTest
  @ValueSource(doubles = {0.5, 0.12, -1.10, 12.201, 0.0, -1})
  void given_a_value_then_is_returned_with_right_format(Double value)
    throws Exception {
    // Given
    given(useCase.calculateIfNotPresent(value))
      .willReturn(value);

    // Expect
    mockMvc.perform(get("/doubles/decimal_comma")
        .queryParam("value", String.valueOf(value)))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(openApiValidator)
      .andExpect(jsonPath("$.value").exists())
      .andExpect(jsonPath("$.value").isString())
      .andExpect(jsonPath("$.value", is(Double.toString(value).replace(".", ","))))
      .andExpect(jsonPath("$.value", matchesPattern("^-?[0-9]+,?[0-9]*$")));
  }
}