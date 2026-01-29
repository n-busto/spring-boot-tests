package com.nbusto.spring.boot.poc.infra.kafka;

import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

/**
 * Context to launch kafka test.
 * <p>
 * Creates a docker image from docker file to simulate an environment.
 * </p>
 * <p>
 * Creates a kafka consumer to be used in producer tests.
 * </p>
 */
@SpringBootTest(classes = SpringBootTestsApplication.class)
public abstract class KafkaTestContext {
  static final ComposeContainer COMPOSE_CONTAINER = new ComposeContainer(
    new File("src/testFixtures/resources/testing-docker-compose.yml"))
    .withEnv("CP_VERSION", "7.6.5")
    .withExposedService("kafka", 9092)
    .withExposedService("schema-registry", 8081)
    .waitingFor("schema-registry", Wait.forHttp("/subjects").forStatusCode(200));

  static {
    COMPOSE_CONTAINER.start();
  }

  protected final KafkaTestConsumer consumer = new KafkaTestConsumer(
    buildBoostrapServers(),
    buildSchemaRegistryServerUri()
  );

  @DynamicPropertySource
  static void registerContainerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.cloud.stream.kafka.binder.producer-properties.schema.registry.url", KafkaTestContext::buildSchemaRegistryServerUri);
    registry.add("spring.cloud.stream.kafka.binder.consumer-properties.schema.registry.url", KafkaTestContext::buildSchemaRegistryServerUri);
    registry.add("spring.cloud.stream.kafka.binder.brokers", KafkaTestContext::buildBoostrapServers);
  }

  protected static @NotNull String buildSchemaRegistryServerUri() {
    return "http://" +
      COMPOSE_CONTAINER.getServiceHost("schema-registry", 8081) +
      ":" +
      COMPOSE_CONTAINER.getServicePort("schema-registry", 8081);
  }

  protected static @NotNull String buildBoostrapServers() {
    return COMPOSE_CONTAINER.getServiceHost("kafka", 9092) +
      ":" +
      COMPOSE_CONTAINER.getServicePort("kafka", 9092);
  }
}