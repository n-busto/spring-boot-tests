package com.nbusto.spring.boot.poc.infra.doubles.controller;

import com.nbusto.spring.boot.poc.application.status.usecase.CalculateDoubleUseCase;
import com.nbusto.spring.boot.poc.infra.doubles.response.DefaultDoubleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doubles/default")
@RequiredArgsConstructor
public class DefaultDoublesController {

  private final CalculateDoubleUseCase useCase;

  @GetMapping
  public DefaultDoubleResponse formatDouble(
    @RequestParam(required = false) Double value
  ) {
    return DefaultDoubleResponse
      .fromDouble(useCase.calculateIfNotPresent(value));
  }
}
