package com.nbusto.spring.boot.poc.application.injection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.instancio.Instancio;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringMother {

  public static List<String> randomList() {
    return Instancio.createList(String.class);
  }

  public static String random() {
    return Instancio.create(String.class);
  }
}
