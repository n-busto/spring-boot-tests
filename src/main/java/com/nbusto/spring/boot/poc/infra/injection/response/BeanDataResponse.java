package com.nbusto.spring.boot.poc.infra.injection.response;

import java.util.List;

public record BeanDataResponse(Integer numberOfBeans, List<String> beanNames) {

  public static BeanDataResponse fromBeanNameList(List<String> beans) {
    return new BeanDataResponse(
      beans.size(),
      beans
    );
  }
}
