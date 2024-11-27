package com.nbusto.spring.boot.poc.infra.validation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record TestRequest(
  @NotNull
  @Valid
  InnerClass innerClass,
  @NotEmpty
  List<String> array,
  @NotEmpty
  String string,
  @NotNull
  @PositiveOrZero
  Integer integer,
  @NotNull
  @PositiveOrZero
  Double doubleValue
) {
}
