package com.nbusto.spring.boot.poc.infra.annotations;

import com.nbusto.spring.boot.poc.infra.controller.ControllerTestConfig;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@WebMvcTest
@Import(ControllerTestConfig.class)
@ContextConfiguration(classes = SpringBootTestsApplication.class)
public @interface ControllerTest {
  @AliasFor(attribute = "controllers", annotation = WebMvcTest.class)
  Class<?>[] controllers();
}
