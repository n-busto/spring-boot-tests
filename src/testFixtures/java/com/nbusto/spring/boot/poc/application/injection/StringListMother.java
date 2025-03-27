package com.nbusto.spring.boot.poc.application.injection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.instancio.Instancio;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringListMother {

  public static List<String> random() {
    return Instancio.createList(String.class);
  }
}
