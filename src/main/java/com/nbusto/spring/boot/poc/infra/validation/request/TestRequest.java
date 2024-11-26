package com.nbusto.spring.boot.poc.infra.validation.request;

import java.util.List;

public record TestRequest(
  InnerClass innerClass,
  List<String> array,
  String string,
  Integer integer,
  Double doubleValue
) {
}
