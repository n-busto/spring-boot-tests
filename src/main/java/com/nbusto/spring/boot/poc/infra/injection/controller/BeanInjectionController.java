package com.nbusto.spring.boot.poc.infra.injection.controller;

import com.nbusto.spring.boot.poc.application.injection.usecase.BeanInjectionUseCase;
import com.nbusto.spring.boot.poc.infra.injection.response.BeanDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/injection")
@RequiredArgsConstructor
public class BeanInjectionController {

  private final BeanInjectionUseCase useCase;

  @GetMapping
  public BeanDataResponse getBeans() {
    return BeanDataResponse
      .fromBeanNameList(useCase.getBeanNames());
  }
}
