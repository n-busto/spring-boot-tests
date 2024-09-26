package com.nbusto.spring.boot.poc.infra.status.response;

import com.nbusto.spring.boot.poc.domain.status.Health;

public record HealthResponse(String status) {

  public static HealthResponse fromDomain(Health in) {
    return new HealthResponse(in.status());
  }
}
