package com.nbusto.spring.boot.poc.infra.status.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HealthAdapter.class)
class HealthAdapterTest {

  @Autowired
  private HealthAdapter adapter;

  @Test
  void given_status_is_up() {
    // Expect
    then(adapter.getHealth())
      .isNotNull()
      .matches(it -> it.status().equals("UP"));
  }
}
