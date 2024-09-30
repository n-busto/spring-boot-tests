package com.nbusto.spring.boot.poc.regresion.status;

import com.nbusto.spring.boot.poc.regresion.BaseKarateTest;

class HealthCheckTest extends BaseKarateTest {
  @Override
  protected String getFeatureName() {
    return "health-check.feature";
  }
}
