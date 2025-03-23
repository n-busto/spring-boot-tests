package com.nbusto.spring.boot.poc.application.dates.usecase;

import java.util.Date;

public class ActualJavaUtilDateGenerator implements DateGenerator<Date> {

  @Override
  public Date generateDate() {
    return new Date();
  }
}
