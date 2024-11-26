package com.nbusto.spring.boot.poc.infra.request;

import com.nbusto.spring.boot.poc.infra.validation.request.InnerClass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.instancio.Instancio.of;
import static org.instancio.Select.field;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InnerClassMother {

  public static InnerClass random() {
    return of(InnerClass.class)
      .generate(field(InnerClass::integer), it -> it.ints().min(0))
      .generate(field(InnerClass::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static InnerClass withNullString() {
    return of(InnerClass.class)
      .ignore(field(InnerClass::string))
      .generate(field(InnerClass::integer), it -> it.ints().min(0))
      .generate(field(InnerClass::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static InnerClass withEmptyString() {
    return of(InnerClass.class)
      .setBlank(field(InnerClass::string))
      .generate(field(InnerClass::integer), it -> it.ints().min(0))
      .generate(field(InnerClass::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static InnerClass withNullInteger() {
    return of(InnerClass.class)
      .ignore(field(InnerClass::integer))
      .generate(field(InnerClass::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static InnerClass withNegativeInteger() {
    return of(InnerClass.class)
      .generate(field(InnerClass::integer), it -> it.ints().max(0))
      .generate(field(InnerClass::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static InnerClass withNullDouble() {
    return of(InnerClass.class)
      .generate(field(InnerClass::integer), it -> it.ints().max(0))
      .ignore(field(InnerClass::doubleValue))
      .create();
  }

  public static InnerClass withNegativeDouble() {
    return of(InnerClass.class)
      .generate(field(InnerClass::integer), it -> it.ints().max(0))
      .generate(field(InnerClass::doubleValue), it -> it.doubles().max(0.0))
      .create();
  }
}
