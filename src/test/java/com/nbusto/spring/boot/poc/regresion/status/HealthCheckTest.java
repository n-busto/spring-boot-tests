package com.nbusto.spring.boot.poc.regresion.status;

import com.nbusto.spring.boot.poc.regresion.BaseKarateTest;

public class HealthCheckTest extends BaseKarateTest {
  @Override
  protected String getScenarioName() {
    return "health-check.feature";
  }
}
