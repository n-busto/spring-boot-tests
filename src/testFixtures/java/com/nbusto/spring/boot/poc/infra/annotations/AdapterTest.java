package com.nbusto.spring.boot.poc.infra.annotations;

import com.nbusto.spring.boot.poc.infra.status.adapter.HealthAdapter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HealthAdapter.class)
public @interface AdapterTest {
  @AliasFor(attribute = "classes", annotation = ContextConfiguration.class)
  Class<?>[] classes();
}
