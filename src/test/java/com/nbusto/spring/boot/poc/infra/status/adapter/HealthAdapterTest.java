package com.nbusto.spring.boot.poc.infra.status.adapter;

import com.nbusto.spring.boot.poc.infra.annotations.AdapterTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.BDDAssertions.then;

@AdapterTest(classes = HealthAdapter.class)
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
