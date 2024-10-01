package com.nbusto.spring.boot.poc.infra.controller;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.atlassian.oai.validator.OpenApiInteractionValidator.createForInlineApiSpecification;
import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseControllerTest {

  public static final String
    CONTRACT_YAML_PATH = "classpath:static/test-contract.yaml";

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private MockMvc mockMvc;

  protected OpenApiInteractionValidator createValidator() throws IOException {
    return createForInlineApiSpecification(
      resourceLoader.getResource(CONTRACT_YAML_PATH)
        .getContentAsString(Charset.defaultCharset()))
      .build();
  }

  protected ResultActions performAction(
    MockHttpServletRequestBuilder action, int status) throws Exception {
    return mockMvc.perform(action)
      .andExpect(status().is(status))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(openApi().isValid(createValidator()));
  }
}
