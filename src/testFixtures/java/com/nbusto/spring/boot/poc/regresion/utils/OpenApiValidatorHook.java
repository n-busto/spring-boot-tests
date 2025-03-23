package com.nbusto.spring.boot.poc.regresion.utils;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.intuit.karate.RuntimeHook;
import com.intuit.karate.core.ScenarioRuntime;
import com.intuit.karate.http.HttpRequest;
import com.intuit.karate.http.Response;

import java.util.List;

// FIXME: we don't validate querying by non existing parameters
public class OpenApiValidatorHook implements RuntimeHook {

  private final OpenApiInteractionValidator validator;

  public OpenApiValidatorHook(OpenApiInteractionValidator validator) {
    this.validator = validator;
  }

  @Override
  public void afterHttpCall(HttpRequest request, Response response, ScenarioRuntime sr) {
    final var report = validator.validate(
      mapRequest(request.toRequest()),
      mapResponse(response));

    if (report.hasErrors()) {
      throw new RuntimeException("OpenAPI response validation failed: " + report.getMessages());
    }
  }

  private com.atlassian.oai.validator.model.Response mapResponse(Response response) {
    return new SimpleResponse.Builder(response.getStatus())
      .withBody(response.getBody())
      .build();
  }

  private Request mapRequest(com.intuit.karate.http.Request request) {
    final var response = new SimpleRequest.Builder(
      request.getMethod(), request.getPath())
      .withBody(request.getBody());

    request.getParams().forEach(response::withQueryParam);
    request.getHeaders()
      .forEach((key, values) -> response.withQueryParam(key, List.copyOf(values)));

    return response.build();
  }
}
