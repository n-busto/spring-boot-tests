package com.nbusto.spring.boot.poc.spring.status;

import com.nbusto.spring.boot.poc.application.status.port.HealthPort;
import com.nbusto.spring.boot.poc.application.status.usecase.HealthCheckUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBeanConfiguration {

  @Bean
  public HealthCheckUseCase configureHealthCheckUseCase(HealthPort port) {
    return new HealthCheckUseCase(port);
  }
}
