package com.nbusto.spring.boot.poc.application.dates.usecase;

public interface DateGenerator<T> {
  T generateDate();
}
