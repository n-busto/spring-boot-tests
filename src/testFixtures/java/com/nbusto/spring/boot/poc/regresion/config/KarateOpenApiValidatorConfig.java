package com.nbusto.spring.boot.poc.regresion.config;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.intuit.karate.RuntimeHook;
import com.nbusto.spring.boot.poc.regresion.utils.OpenApiValidatorHook;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.Charset;

@TestConfiguration
public class KarateOpenApiValidatorConfig {

  @Bean
  public RuntimeHook getOpenApiValidatorHook(ResourceLoader resourceLoader) throws IOException {
    return new OpenApiValidatorHook(
      OpenApiInteractionValidator
        .createFor(resourceLoader
          .getResource("classpath:static/test-contract.yaml")
          .getContentAsString(Charset.defaultCharset()))
        .withParseOptions(getOpenApiValidatorOptions())
        .build());
  }

  private ParseOptions getOpenApiValidatorOptions() {
    final var options = new ParseOptions();
    options.setResolve(true);
    options.setResolveFully(true);
    options.setResolveCombinators(true);
    options.setAllowEmptyString(false);

    return options;
  }
}
