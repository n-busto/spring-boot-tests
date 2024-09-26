package com.nbusto.spring.boot.poc.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
  scanBasePackages = {
    "com.nbusto.spring.boot.poc.infra",
    "com.nbusto.spring.boot.poc.spring"
  }
)
public class SpringBootTestsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootTestsApplication.class, args);
  }

}
