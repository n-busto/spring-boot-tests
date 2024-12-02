package com.nbusto.spring.boot.poc.infra.validation.controller;

import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.sources.files.FileSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = ValidationTestController.class)
public class ValidationTestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @ParameterizedTest
  @FileSource("requests/validation/wrong")
  void when_request_is_not_valid_expect_badRequest(String request) throws Exception {
    // Expect
    mockMvc.perform(post("/validation")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void when_request_is_valid_expect_OK() throws Exception {
    // Given
    final var request = """
      {
        "innerObject": {
          "string":"EXWUDSIHH",
          "integer":6606,
          "doubleValue":-1.0957806279504516E-5
        },
        "array":["NKRYDM","VOQD","JLIU"],
        "string":"MWNELCJ",
        "integer":9063,
        "doubleValue":8743.242551784579}
      """;

    // Then
    mockMvc.perform(post("/validation")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}
