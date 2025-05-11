package com.nbusto.spring.boot.poc.infra.kafka;


import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@DirtiesContext
@Testcontainers
public abstract class KafkaTestContext {

  @Container
  static final ConfluentKafkaContainer KAFKA_CONTAINER =
    new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.5"));

  private static Map<String, Object> CONSUMER_PROPS;

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    registry.add("spring.kafka.producer.properties.schema.registry.url", () -> "http://localhost:8081/");
  }

  @BeforeAll
  static void beforeAll() {
    CONSUMER_PROPS = Map.of(
      "schema.registry.url", "http://localhost:8081/",
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

  protected <T> Consumer<String, T> createConsumer() {
    final var consumerFactory = new DefaultKafkaConsumerFactory<String, T>(CONSUMER_PROPS);

    return consumerFactory.createConsumer();
  }

}
