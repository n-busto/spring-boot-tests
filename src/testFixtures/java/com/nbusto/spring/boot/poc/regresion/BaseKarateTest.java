package com.nbusto.spring.boot.poc.regresion;

import com.intuit.karate.junit5.Karate;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

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

  @Autowired
  private ResourceLoader resourceLoader;

  @Karate.Test
  Karate testFeature() throws IOException {
    return Karate.run("classpath:karate/scenarios/" + getFeatureName())
      .failWhenNoScenariosFound(true)
      .hook(new OpenApiValidatorHook(resourceLoader))
      .systemProperty("karate.server.port", webContext.getWebServer().getPort() + "");
  }

  protected abstract String getFeatureName();
}
