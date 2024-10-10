package com.nbusto.spring.boot.poc.application.doubles.usecase;

import com.nbusto.spring.boot.poc.application.ApplicationTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;

@ApplicationTest
class CalculateDoubleUseCaseTest {

  private final CalculateDoubleUseCase sut = new CalculateDoubleUseCase();

  @RepeatedTest(100)
  void given_null_input_returns_a_random_double() {
    // Expect
    then(sut.calculateIfNotPresent(null))
      .isNotNull()
      .isBetween(-50.0, 100.0);
  }

  @ParameterizedTest
  @ValueSource(doubles = {10.2, 1.03, -0.5, 0.0})
  void given_a_expected_value_is_returned(double value) {
    // Expect
    then(sut.calculateIfNotPresent(value))
      .isNotNull()
      .isEqualTo(value);
  }

}