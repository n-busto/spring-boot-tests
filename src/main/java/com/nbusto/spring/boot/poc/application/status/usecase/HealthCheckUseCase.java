package com.nbusto.spring.boot.poc.application.status.usecase;

import com.nbusto.spring.boot.poc.application.status.port.HealthPort;
import com.nbusto.spring.boot.poc.domain.status.Health;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HealthCheckUseCase {

  private final HealthPort port;

  public Health getHealth() {
    return port.getHealth();
  }
}
