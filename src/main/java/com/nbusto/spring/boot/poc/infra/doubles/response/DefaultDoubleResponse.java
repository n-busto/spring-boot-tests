package com.nbusto.spring.boot.poc.infra.doubles.response;

public record DefaultDoubleResponse(Double value) {

  public static DefaultDoubleResponse fromDouble(Double value) {
    return new DefaultDoubleResponse(value);
  }
}
