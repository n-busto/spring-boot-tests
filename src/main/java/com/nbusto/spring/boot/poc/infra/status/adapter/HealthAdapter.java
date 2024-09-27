package com.nbusto.spring.boot.poc.infra.status.adapter;

import com.nbusto.spring.boot.poc.application.status.port.HealthPort;
import com.nbusto.spring.boot.poc.domain.status.Health;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HealthAdapter implements HealthPort {

  @Override
  public Health getHealth() {
    return new Health("UP");
  }
}
