package com.nbusto.spring.boot.poc.spring.doubles;

import com.nbusto.spring.boot.poc.application.doubles.usecase.CalculateDoubleUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DoublesBeanConfiguration {

  @Bean
  public CalculateDoubleUseCase getCalculateDoubleUseCase() {
    return new CalculateDoubleUseCase();
  }
}
