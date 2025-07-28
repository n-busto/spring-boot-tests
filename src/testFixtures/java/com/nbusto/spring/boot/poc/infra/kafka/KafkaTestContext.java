package com.nbusto.spring.boot.poc.infra.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Map;

@Testcontainers
public abstract class KafkaTestContext {

  @Container
  static final ComposeContainer CONTAINERISED_DOCKER_COMPOSE = new ComposeContainer(new File("src/testFixtures/resources/compose/docker-compose.yml"))
    .withExposedService("kafka-broker", 9092)
    .withExposedService("schema-registry", 8081)
    .withEnv("CP_VERSION", "7.6.5");

  private static @NotNull String getServiceHost(String serviceName, int servicePort) {
    return CONTAINERISED_DOCKER_COMPOSE.getServiceHost(serviceName, servicePort) + ":" + CONTAINERISED_DOCKER_COMPOSE.getServicePort(serviceName, servicePort);
  }

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", () -> getServiceHost("schema-registry", 8081));
    registry.add("spring.kafka.producer.properties.schema.registry.url", () -> getServiceHost("schema-registry", 8081));
    registry.add("schema.registry.url", () -> getServiceHost("schema-registry", 8081));
  }

  protected <T> Consumer<String, T> createConsumer() {
    final var properties = Map.<String, Object>of(
      "schema.registry.url", getServiceHost("schema-registry", 8081),
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getServiceHost("kafka-broker", 9092),
      ConsumerConfig.GROUP_ID_CONFIG, "testConsumer",
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
      ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
      ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
      KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

    return new DefaultKafkaConsumerFactory<String, T>(properties).createConsumer();
  }
}