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

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = DefaultDoublesController.class)
class DefaultDoublesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CalculateDoubleUseCase useCase;

  @Test
  void when_not_provided_value_then_random_value_is_returned_with_right_format()
    throws Exception {
    // Given
    given(useCase.calculateIfNotPresent(null))
      .willReturn(123.45);

    // Expect
    final var result = mockMvc.perform(get("/doubles/default")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.value").exists())
      .andExpect(jsonPath("$.value").isNumber())
      .andExpect(jsonPath("$.value", equalTo(123.45)))
      .andReturn()
      .getResponse()
      .getContentAsString();

    then(result).matches("^.*[0-9]*[.][0-9]*}$");
  }

  @ParameterizedTest
  @ValueSource(doubles = {0.5, 0.12, -1.10, 12.20, 0.0, -1})
  void given_a_value_then_is_returned_with_right_format(Double value)
    throws Exception {
    // Given
    given(useCase.calculateIfNotPresent(value))
      .willReturn(value);

    // Expect
    final var result = mockMvc.perform(get("/doubles/default")
        .queryParam("value", String.valueOf(value))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.value").exists())
      .andExpect(jsonPath("$.value").isNumber())
      .andExpect(jsonPath("$.value", equalTo(value)))
      .andReturn()
      .getResponse()
      .getContentAsString();

    then(result).matches("^.*[0-9]*[.][0-9]*}$");
  }
}