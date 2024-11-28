package com.nbusto.spring.boot.poc.infra.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.atlassian.oai.validator.OpenApiInteractionValidator.createForInlineApiSpecification;
import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;

@TestConfiguration
public class ControllerTestConfig {

  @Bean
  public ResultMatcher createOpenApiValidator(ResourceLoader resourceLoader)
    throws IOException {
    return openApi().isValid(createForInlineApiSpecification(
      resourceLoader.getResource("classpath:static/test-contract.yaml")
        .getContentAsString(Charset.defaultCharset()))
      .build());
  }
}
