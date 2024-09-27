package com.nbusto.spring.boot.poc.infra.annotations;

import com.nbusto.spring.boot.poc.infra.status.controller.HealthCheckController;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@WebMvcTest(controllers = HealthCheckController.class)
@ContextConfiguration(classes = SpringBootTestsApplication.class)
public @interface ControllerTest {
  @AliasFor(attribute = "controllers", annotation = WebMvcTest.class)
  Class<?>[] controllers();
}
