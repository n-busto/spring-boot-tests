package com.nbusto.spring.boot.poc.domain.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.instancio.Instancio;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMother {

  public static Order random() {
    return Instancio.create(Order.class);
  }

}
