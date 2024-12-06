package com.nbusto.spring.boot.poc.regresion;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.core.ScenarioRuntime;
import com.intuit.karate.http.HttpRequest;
import com.intuit.karate.http.HttpRequestBuilder;
import com.intuit.karate.http.Response;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class OpenApiValidatorHook implements RuntimeHook {

  //TODO improve validation

  private final OpenApiInteractionValidator validator;

  public OpenApiValidatorHook(ResourceLoader resourceLoader) throws IOException {
    this.validator =
      OpenApiInteractionValidator.createFor(resourceLoader.getResource("classpath:static/test-contract.yaml").getContentAsString(Charset.defaultCharset())).build();
  }

  @Override
  public void afterHttpCall(HttpRequest request, Response response, ScenarioRuntime sr) {
    final var path = request.getUrl().substring(request.getUrl().indexOf("/", 10));
    validateRequest(path, request.getMethod(), request.getBodyAsString());
    validateResponse(path, request.getMethod(), response.getBodyAsString(), response.getStatus());
  }

  private void validateResponse(String path, String method, String responseBody, int statusCode) {
    final var response = new SimpleResponse.Builder(statusCode)
      .withBody(responseBody)
      .build();

    final var report = validator.validateResponse(path, Request.Method.valueOf(method), response);
    if (report.hasErrors()) {
      throw new RuntimeException("OpenAPI response validation failed: " + report);
    }
  }

  private void validateRequest(String path, String method, String requestBody) {
    final var request = new SimpleRequest.Builder(method, path)
      .withBody(requestBody)
      .build();

    final var report = validator.validateRequest(request);
    if (report.hasErrors()) {
      throw new RuntimeException("OpenAPI request validation failed: " + report);
    }
  }
}
