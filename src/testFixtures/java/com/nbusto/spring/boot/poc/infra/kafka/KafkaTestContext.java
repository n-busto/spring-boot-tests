package com.nbusto.spring.boot.poc.infra.kafka;

import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Collections;
import java.util.Map;

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

  @DynamicPropertySource
  static void registerContainerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.properties.schema.registry.url", KafkaTestContext::buildSchemaRegistryServerUri);
    registry.add("spring.kafka.bootstrap-servers", KafkaTestContext::buildBoostrapServers);
  }

  private static Map<String, Object> consumerProps() {
    final var properties = KafkaTestUtils.consumerProps(buildBoostrapServers(), "test-group", "true");

    properties.put("schema.registry.url", buildSchemaRegistryServerUri());
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

    return properties;
  }

  private static @NotNull String buildSchemaRegistryServerUri() {
    return "http://localhost:" + COMPOSE_CONTAINER.getServicePort("schema-registry", 8081);
  }

  private static @NotNull String buildBoostrapServers() {
    return "localhost:" + COMPOSE_CONTAINER.getServicePort("kafka", 9092);
  }

  protected static <T> Consumer<String, T> createConsumer(final String topic) {
    final var consumer = new DefaultKafkaConsumerFactory<String, T>(consumerProps()).createConsumer();

    consumer.subscribe(Collections.singletonList(topic));

    return consumer;
  }
}