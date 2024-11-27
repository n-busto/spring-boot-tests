package com.nbusto.spring.boot.poc.infra.validation.request;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InnerClass(
  @NotEmpty
  String string,
  @NotNull
  @PositiveOrZero
  Integer integer,
  @NotNull
  @Negative
  Double doubleValue
) {
}