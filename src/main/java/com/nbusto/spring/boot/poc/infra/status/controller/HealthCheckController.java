package com.nbusto.spring.boot.poc.infra.status.controller;

import com.nbusto.spring.boot.poc.application.status.usecase.HealthCheckUseCase;
import com.nbusto.spring.boot.poc.infra.status.response.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
@RequiredArgsConstructor
public class HealthCheckController {

  @Autowired
  private final HealthCheckUseCase useCase;

  @GetMapping
  public HealthResponse findStatusResponse() {
    return HealthResponse.fromDomain(useCase.getHealth());
  }
}
