package com.nbusto.spring.boot.poc.spring.status;

import com.nbusto.spring.boot.poc.application.status.usecase.HealthCheckUseCase;
import com.nbusto.spring.boot.poc.domain.status.Health;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatusBeanConfiguration {

  @Bean
  public HealthCheckUseCase configureHealthCheckUseCase() {
    return new HealthCheckUseCase(() -> new Health("UP"));
  }
}
