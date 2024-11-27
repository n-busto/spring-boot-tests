package com.nbusto.spring.boot.poc.infra.validation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.infra.controller.BaseControllerTest;
import com.nbusto.spring.boot.poc.infra.request.InnerClassMother;
import com.nbusto.spring.boot.poc.infra.request.TestRequestMother;
import com.nbusto.spring.boot.poc.infra.validation.request.TestRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ControllerTest(controllers = ValidationTestController.class)
public class ValidationTestControllerTest extends BaseControllerTest {

  @Autowired
  private ObjectMapper mapper;

  private static Stream<TestRequest> invalidRequestGenerator() {
    return Stream.of(
      null,
      TestRequestMother.withNullInnerObject(),
      TestRequestMother.withInnerObject(InnerClassMother.withEmptyString()),
      TestRequestMother.withInnerObject(InnerClassMother.withNullString()),
      TestRequestMother.withInnerObject(InnerClassMother.withNullInteger()),
      TestRequestMother.withInnerObject(InnerClassMother.withNegativeInteger()),
      TestRequestMother.withInnerObject(InnerClassMother.withNullDouble()),
      TestRequestMother.withInnerObject(InnerClassMother.withNegativeDouble()),
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

  @Test
  void when_request_is_valid_expect_OK() throws Exception {
    // Given
    final var postRequest = post("/validation")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsBytes(TestRequestMother.withInnerObject(InnerClassMother.random())));

    // Then
    performAction(postRequest, HttpStatus.SC_OK);
  }

  @ParameterizedTest
  @MethodSource("invalidRequestGenerator")
  void when_request_is_not_valid_expect_badRequest(TestRequest request) throws Exception {
    // Given
    final var postRequest = post("/validation")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsBytes(request));

    // Then
    performAction(postRequest, HttpStatus.SC_BAD_REQUEST);
  }
}
