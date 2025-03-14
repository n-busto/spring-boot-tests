package com.nbusto.spring.boot.poc.spring.injection;

import com.nbusto.spring.boot.poc.application.injection.port.BeanPort;
import com.nbusto.spring.boot.poc.application.injection.usecase.BeanInjectionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanInjectionBeanConfiguration {

  @Bean
  public BeanInjectionUseCase getBeanInjectionUseCase(List<BeanPort> beanList) {
    return new BeanInjectionUseCase(beanList);
  }

  @Bean
  public BeanPort getBeanPortA() {
    return () -> "A";
  }

  @Bean
  public BeanPort getBeanPortB() {
    return () -> "B";
  }

  @Bean
  public BeanPort getBeanPortC() {
    return () -> "C";
  }
}
