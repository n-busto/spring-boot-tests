package com.nbusto.spring.boot.poc.application.status.port;

import com.nbusto.spring.boot.poc.domain.status.Health;

public interface HealthPort {
  Health getHealth();
}
