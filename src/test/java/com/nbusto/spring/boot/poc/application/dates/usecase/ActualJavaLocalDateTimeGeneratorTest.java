package com.nbusto.spring.boot.poc.application.dates.usecase;

import com.nbusto.spring.boot.poc.application.ApplicationTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;

@ApplicationTest
public class ActualJavaLocalDateTimeGeneratorTest {

  private final DateGenerator<LocalDateTime> sut = new ActualJavaLocalDateTimeGenerator();

  @Test
  void when_date_is_generated_is_close_to_now() {
    // Expect
    then(sut.generateDate())
      .isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
  }

}
