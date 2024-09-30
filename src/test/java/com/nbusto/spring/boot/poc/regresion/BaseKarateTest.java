package com.nbusto.spring.boot.poc.regresion;

import com.intuit.karate.junit5.Karate;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = SpringBootTestsApplication.class)
@TestPropertySource(
  properties = {
    "spring.main.log-startup-info=OFF",
    "logging.level.root=error"
  }
)
public abstract class BaseKarateTest {

  @Autowired
  private ServletWebServerApplicationContext webContext;

  @Karate.Test
  Karate testFeature() {
    return Karate.run("classpath:karate/scenarios/" + getFeatureName())
      .failWhenNoScenariosFound(true)
      .systemProperty("karate.server.port", webContext.getWebServer().getPort() + "");
  }

  protected abstract String getFeatureName();
}
