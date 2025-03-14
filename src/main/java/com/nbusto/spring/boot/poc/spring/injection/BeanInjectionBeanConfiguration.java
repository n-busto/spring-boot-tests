package com.nbusto.spring.boot.poc.spring.injection;

import com.nbusto.spring.boot.poc.application.injection.usecase.BeanInjectionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInjectionBeanConfiguration {

  @Bean
  public BeanInjectionUseCase getBeanInjectionUseCase() {
    return new BeanInjectionUseCase();
  }
}
