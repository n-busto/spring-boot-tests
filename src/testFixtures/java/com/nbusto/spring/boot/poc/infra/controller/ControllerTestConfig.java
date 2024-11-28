package com.nbusto.spring.boot.poc.infra.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.atlassian.oai.validator.OpenApiInteractionValidator.createForInlineApiSpecification;
import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;

@TestConfiguration
public class ControllerTestConfig {

  public ResultMatcher createOpenApiValidator(ResourceLoader resourceLoader)
    throws IOException {
    return openApi().isValid(createForInlineApiSpecification(
      resourceLoader.getResource("classpath:static/test-contract.yaml")
        .getContentAsString(Charset.defaultCharset()))
      .build());
  }

  @Bean
  public MockMvc createCustomMvc(WebApplicationContext context, ResourceLoader resourceLoader) {
    return MockMvcBuilders.webAppContextSetup(context)
      .alwaysDo(it -> {
        if (it.getResponse().getStatus() == HttpServletResponse.SC_OK) {
          createOpenApiValidator(resourceLoader).match(it);
        }
      })
      .build();
  }
}
