package com.nbusto.spring.boot.poc.application.status.usecase;

import com.nbusto.spring.boot.poc.domain.status.Health;

public class HealthCheckUseCase {

  public Health getHealth() {
    return new Health("UP");
  }
}
