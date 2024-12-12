package com.nbusto.spring.boot.poc.regresion;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.core.ScenarioRuntime;
import com.intuit.karate.http.HttpRequest;
import com.intuit.karate.http.Response;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class OpenApiValidatorHook implements RuntimeHook {

  //TODO improve validation

  private final OpenApiInteractionValidator validator;

  public OpenApiValidatorHook(ResourceLoader resourceLoader) throws IOException {
    this.validator = OpenApiInteractionValidator
      .createFor(resourceLoader
        .getResource("classpath:static/test-contract.yaml")
        .getContentAsString(Charset.defaultCharset()))
      .build();
  }

  @Override
  public void afterHttpCall(HttpRequest request, Response response, ScenarioRuntime sr) {
    final var path = cleanUrl(request.getUrl());

    validate(
      calculateRequest(path, request.getMethod(), request.getBodyAsString(), extractQueryParams(request.getUrl())),
      calculateResponse(response.getBodyAsString(), response.getStatus()));
  }

  private Map<String, String> extractQueryParams(String url) {
    final var queryParams = new HashMap<String, String>();
    if (url.contains("?")) {
      final var queryParts = url.substring(url.indexOf("?") + 1).split("&");
      for (String queryPart : queryParts) {
        final var keyValue = queryPart.split("=");
        queryParams.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
      }
    }
    return queryParams;
  }

  private com.atlassian.oai.validator.model.Response calculateResponse(String responseBody, int statusCode) {
    return new SimpleResponse.Builder(statusCode)
      .withBody(responseBody)
      .build();
  }

  private void validate(Request request, com.atlassian.oai.validator.model.Response response) {
    final var report = validator.validate(request, response);
    if (report.hasErrors()) {
      throw new RuntimeException("OpenAPI response validation failed: " + report);
    }
  }

  private Request calculateRequest(
    String path,
    String method,
    String requestBody,
    Map<String, String> queryParams) {
    final var request = new SimpleRequest.Builder(method, path)
      .withBody(requestBody);

    queryParams.forEach(request::withQueryParam);

    return request.build();
  }

  private String cleanUrl(String url) {
    var cleanUrl = url.substring(url.indexOf("/", 10));
    if (cleanUrl.contains("?")) {
      cleanUrl = cleanUrl.substring(0, cleanUrl.indexOf("?"));
    }
    return cleanUrl;
  }
}
