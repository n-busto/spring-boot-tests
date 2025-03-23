package com.nbusto.spring.boot.poc.application.dates.usecase;

import com.nbusto.spring.boot.poc.application.ApplicationTest;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.BDDAssertions.then;

@ApplicationTest
public class ActualJavaUtilDateGeneratorTest {

  private final DateGenerator<Date> sut = new ActualJavaUtilDateGenerator();

  @Test
  void when_date_is_generated_is_close_to_now() {
    // Expect
    then(sut.generateDate())
      .isCloseTo(new Date(), 100);
  }

}
