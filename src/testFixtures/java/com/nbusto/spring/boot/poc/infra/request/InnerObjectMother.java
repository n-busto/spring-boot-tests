package com.nbusto.spring.boot.poc.infra.request;

import com.nbusto.spring.boot.poc.infra.validation.request.InnerObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.instancio.Instancio.of;
import static org.instancio.Select.field;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InnerObjectMother {

  public static InnerObject random() {
    return of(InnerObject.class)
      .generate(field(InnerObject::integer), it -> it.ints().min(0))
      .generate(field(InnerObject::doubleValue), it -> it.doubles().max(-0.00001))
      .create();
  }

  public static InnerObject withNullString() {
    return of(InnerObject.class)
      .ignore(field(InnerObject::string))
      .generate(field(InnerObject::integer), it -> it.ints().min(0))
      .generate(field(InnerObject::doubleValue), it -> it.doubles().max(-0.00001))
      .create();
  }

  public static InnerObject withEmptyString() {
    return of(InnerObject.class)
      .setBlank(field(InnerObject::string))
      .generate(field(InnerObject::integer), it -> it.ints().min(0))
      .generate(field(InnerObject::doubleValue), it -> it.doubles().max(-0.00001))
      .create();
  }

  public static InnerObject withNullInteger() {
    return of(InnerObject.class)
      .ignore(field(InnerObject::integer))
      .generate(field(InnerObject::doubleValue), it -> it.doubles().max(-0.00001))
      .create();
  }

  public static InnerObject withNegativeInteger() {
    return of(InnerObject.class)
      .generate(field(InnerObject::integer), it -> it.ints().max(-1))
      .generate(field(InnerObject::doubleValue), it -> it.doubles().max(-0.00001))
      .create();
  }

  public static InnerObject withNullDouble() {
    return of(InnerObject.class)
      .generate(field(InnerObject::integer), it -> it.ints().min(0))
      .ignore(field(InnerObject::doubleValue))
      .create();
  }

  public static InnerObject withPositiveDouble() {
    return of(InnerObject.class)
      .generate(field(InnerObject::integer), it -> it.ints().min(0))
      .generate(field(InnerObject::doubleValue), it -> it.doubles().min(0.00001))
      .create();
  }
}
