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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.util.Map;

import static org.testcontainers.utility.DockerImageName.parse;

@Testcontainers
@SpringBootTest(classes = SpringBootTestsApplication.class)
public abstract class KafkaTestContext {

  private static final Network NETWORK = Network.newNetwork();

  @Container
  static ConfluentKafkaContainer KAFKA_CONTAINER =
    new ConfluentKafkaContainer(parse("confluentinc/cp-kafka:7.6.5"))
      .withExposedPorts(9092)
      .withNetworkAliases("kafka")
      .withNetwork(NETWORK);

  @Container
  static GenericContainer<?> SCHEMA_REGISTRY_CONTAINER =
    new GenericContainer<>(parse("confluentinc/cp-schema-registry:7.6.5"))
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

  static Map<String, Object> consumerProps() {
    return Map.of("schema.registry.url", buildSchemaRegistryServerUri(),
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers(),
      ConsumerConfig.GROUP_ID_CONFIG, "testConsumer",
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
      ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
      ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
      KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
  }

  private static @NotNull String buildSchemaRegistryServerUri() {
    return "http://localhost:" + SCHEMA_REGISTRY_CONTAINER.getMappedPort(8081);
  }

  protected <T> Consumer<String, T> createConsumer() {
    final var consumerFactory = new DefaultKafkaConsumerFactory<String, T>(consumerProps());

    return consumerFactory.createConsumer();
  }
}