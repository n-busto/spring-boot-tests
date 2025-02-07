package com.nbusto.spring.boot.poc.application.dates.usecase;

import java.time.LocalDateTime;

public class ActualJavaLocalDateTimeGenerator implements DateGenerator<LocalDateTime> {
  @Override
  public LocalDateTime generateDate() {
    return LocalDateTime.now();
  }
}
