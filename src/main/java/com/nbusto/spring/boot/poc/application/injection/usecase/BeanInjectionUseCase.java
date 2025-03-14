package com.nbusto.spring.boot.poc.application.injection.usecase;

import com.nbusto.spring.boot.poc.application.injection.port.BeanPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BeanInjectionUseCase {

  private final List<BeanPort> beanList;

  public List<String> getBeanNames() {
    return beanList.stream()
      .map(BeanPort::getName)
      .toList();
  }
}
