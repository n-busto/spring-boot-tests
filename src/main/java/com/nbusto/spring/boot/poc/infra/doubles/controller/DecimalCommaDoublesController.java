package com.nbusto.spring.boot.poc.infra.doubles.controller;

import com.nbusto.spring.boot.poc.application.doubles.usecase.CalculateDoubleUseCase;
import com.nbusto.spring.boot.poc.infra.doubles.response.DecimalCommaDoubleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doubles/decimal_comma")
@RequiredArgsConstructor
public class DecimalCommaDoublesController {

  private final CalculateDoubleUseCase useCase;

  @GetMapping
  public DecimalCommaDoubleResponse formatDouble(
    @RequestParam(required = false) Double value
  ) {
    return DecimalCommaDoubleResponse
      .fromDouble(useCase.calculateIfNotPresent(value));
  }
}
