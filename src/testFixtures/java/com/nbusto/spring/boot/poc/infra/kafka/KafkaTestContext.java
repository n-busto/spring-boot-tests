package com.nbusto.spring.boot.poc.infra.kafka;

import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.util.Collections;
import java.util.Map;

import static org.testcontainers.utility.DockerImageName.parse;

@Testcontainers
@SpringBootTest(classes = SpringBootTestsApplication.class)
public abstract class KafkaTestContext {

  public static final String CONFLUENT_PLATFORM_VERSION = "7.6.5";

  private static final Network NETWORK = Network.newNetwork();

  @Container
  static ConfluentKafkaContainer KAFKA_CONTAINER =
    new ConfluentKafkaContainer(parse("confluentinc/cp-kafka:" + CONFLUENT_PLATFORM_VERSION))
      .withNetworkAliases("kafka")
      .withNetwork(NETWORK);

  @Container
  static GenericContainer<?> SCHEMA_REGISTRY_CONTAINER =
    new GenericContainer<>(parse("confluentinc/cp-schema-registry:" + CONFLUENT_PLATFORM_VERSION))
      .withNetwork(NETWORK)
      .withExposedPorts(8081)
      .withNetworkAliases("schema-registry")
      .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
      .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
      .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "kafka:9092")
      .dependsOn(KAFKA_CONTAINER);

  @DynamicPropertySource
  static void registerContainerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.properties.schema.registry.url",
      KafkaTestContext::buildSchemaRegistryServerUri);
    registry.add("spring.kafka.bootstrap-servers",
      KAFKA_CONTAINER::getBootstrapServers);
  }

  private static Map<String, Object> consumerProps() {
    final var properties = KafkaTestUtils.consumerProps(
      KAFKA_CONTAINER.getBootstrapServers(),
      "test-group",
      "true"
    );

    properties.put("schema.registry.url", buildSchemaRegistryServerUri());
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

    return properties;
  }

  private static @NotNull String buildSchemaRegistryServerUri() {
    return "http://localhost:" + SCHEMA_REGISTRY_CONTAINER.getMappedPort(8081);
  }

  protected static <T> Consumer<String, T> createConsumer(final String topic) {
    final var consumer = new DefaultKafkaConsumerFactory<String, T>(consumerProps())
      .createConsumer();

    consumer.subscribe(Collections.singletonList(topic));

    return consumer;
  }
}