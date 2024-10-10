package com.nbusto.spring.boot.poc.application.doubles.usecase;

import java.util.Optional;
import java.util.Random;

public class CalculateDoubleUseCase {
  private static double MIN_VALUE = -50.0;
  private static double MAX_VALUE = 100.0;

  public Double calculateIfNotPresent(Double value) {
    return Optional.ofNullable(value)
      .orElse(new Random().nextDouble(MAX_VALUE - MIN_VALUE) + MIN_VALUE);
  }
}
