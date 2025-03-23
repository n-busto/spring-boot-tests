package com.nbusto.spring.boot.poc.regresion;

import com.intuit.karate.RuntimeHook;
import com.intuit.karate.junit5.Karate;
import com.nbusto.spring.boot.poc.regresion.config.KarateOpenApiValidatorConfig;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = SpringBootTestsApplication.class)
@TestPropertySource(
  properties = {
    "spring.main.log-startup-info=OFF",
    "logging.level.root=error"
  }
)
@Import(KarateOpenApiValidatorConfig.class)
public abstract class BaseKarateTest {

  @Autowired
  private ServletWebServerApplicationContext webContext;

  @Autowired
  private List<RuntimeHook> hooks;

  @Karate.Test
  Karate testFeature() throws IOException {
    return Karate.run("classpath:karate/scenarios/" + getFeatureName())
      .failWhenNoScenariosFound(true)
      .hooks(hooks)
      .systemProperty("karate.server.port", webContext.getWebServer().getPort() + "");
  }

  protected abstract String getFeatureName();
}
