package com.nbusto.spring.boot.poc.infra.request;

import com.nbusto.spring.boot.poc.infra.validation.request.InnerClass;
import com.nbusto.spring.boot.poc.infra.validation.request.TestRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.instancio.Instancio.of;
import static org.instancio.Select.field;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestRequestMother {

  public static TestRequest withNullInnerObject() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().min(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .ignore(field(TestRequest::innerClass))
      .create();
  }

  public static TestRequest withInnerObject(InnerClass innerClass) {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().min(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .set(field(TestRequest::innerClass), innerClass)
      .create();
  }

  public static TestRequest withEmptyArray() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().min(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .setBlank(field(TestRequest::array))
      .create();
  }

  public static TestRequest withNullArray() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().min(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .ignore(field(TestRequest::array))
      .create();
  }

  public static TestRequest withEmptyString() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().min(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .setBlank(field(TestRequest::string))
      .create();
  }

  public static TestRequest withNullString() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().min(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .ignore(field(TestRequest::string))
      .create();
  }

  public static TestRequest withNullInteger() {
    return of(TestRequest.class)
      .ignore(field(TestRequest::integer))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static TestRequest withNegativeInteger() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().max(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().min(0.0))
      .create();
  }

  public static TestRequest withNullDouble() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().max(0))
      .ignore(field(TestRequest::doubleValue))
      .create();
  }

  public static TestRequest withNegativeDouble() {
    return of(TestRequest.class)
      .generate(field(TestRequest::integer), it -> it.ints().max(0))
      .generate(field(TestRequest::doubleValue), it -> it.doubles().max(0.0))
      .create();
  }
}
