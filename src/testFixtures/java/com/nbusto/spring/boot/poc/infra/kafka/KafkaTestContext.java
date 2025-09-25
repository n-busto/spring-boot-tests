package com.nbusto.spring.boot.poc.infra.kafka;

import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
@SpringBootTest(classes = SpringBootTestsApplication.class)
public abstract class KafkaTestContext {

  @Container
  static final ComposeContainer COMPOSE_CONTAINER = new ComposeContainer(
    new File("src/testFixtures/resources/testing-docker-compose.yml"))
    .withEnv("CP_VERSION", "7.6.5")
    .withExposedService("kafka", 9092)
    .withExposedService("schema-registry", 8081)
    .waitingFor("schema-registry", Wait.forHttp("/subjects").forStatusCode(200));

  protected final KafkaTestConsumer consumer = new KafkaTestConsumer(
    buildBoostrapServers(),
    buildSchemaRegistryServerUri()
  );

  @DynamicPropertySource
  static void registerContainerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.properties.schema.registry.url", KafkaTestContext::buildSchemaRegistryServerUri);
    registry.add("spring.kafka.bootstrap-servers", KafkaTestContext::buildBoostrapServers);
  }

  private static @NotNull String buildSchemaRegistryServerUri() {
    return "http://localhost:" + COMPOSE_CONTAINER.getServicePort("schema-registry", 8081);
  }

  private static @NotNull String buildBoostrapServers() {
    return "localhost:" + COMPOSE_CONTAINER.getServicePort("kafka", 9092);
  }
}