package com.nbusto.spring.boot.poc.infra.validation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.infra.request.InnerObjectMother;
import com.nbusto.spring.boot.poc.infra.request.TestRequestMother;
import com.nbusto.spring.boot.poc.infra.validation.request.TestRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = ValidationTestController.class)
public class ValidationTestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ResultMatcher openApiValidator;

  @Autowired
  private ObjectMapper mapper;

  private static Stream<TestRequest> invalidRequestGenerator() {
    return Stream.of(
      TestRequestMother.withNullInnerObject(),
      TestRequestMother.withInnerObject(InnerObjectMother.withEmptyString()),
      TestRequestMother.withInnerObject(InnerObjectMother.withNullString()),
      TestRequestMother.withInnerObject(InnerObjectMother.withNullInteger()),
      TestRequestMother.withInnerObject(InnerObjectMother.withNegativeInteger()),
      TestRequestMother.withInnerObject(InnerObjectMother.withNullDouble()),
      TestRequestMother.withInnerObject(InnerObjectMother.withPositiveDouble()),
      TestRequestMother.withNullArray(),
      TestRequestMother.withEmptyArray(),
      TestRequestMother.withEmptyString(),
      TestRequestMother.withNullString(),
      TestRequestMother.withNullInteger(),
      TestRequestMother.withNegativeInteger(),
      TestRequestMother.withNullDouble(),
      TestRequestMother.withNegativeDouble()
    );
  }

  @ParameterizedTest
  @MethodSource("invalidRequestGenerator")
  void when_request_is_not_valid_expect_badRequest(TestRequest request) throws Exception {
    // Expect
    mockMvc.perform(post("/validation")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsBytes(request)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void when_request_is_valid_expect_OK() throws Exception {
    // Given
    final var request = mapper.writeValueAsBytes(TestRequestMother.withInnerObject(InnerObjectMother.random()));

    // Then
    mockMvc.perform(post("/validation")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(openApiValidator);
  }

  @Test
  void when_request_is_is_empty_expect_badRequest() throws Exception {
    // Given
    final var request = "{}";

    // Expect
    mockMvc.perform(post("/validation")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}
