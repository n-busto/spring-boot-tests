package com.nbusto.spring.boot.poc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringBootTestsApplicationTest {

  @Autowired
  ApplicationContext  context;

  @Test
  void given_context_then_is_not_null() {
    Assertions.assertThat(context).isNotNull();
  }

}
