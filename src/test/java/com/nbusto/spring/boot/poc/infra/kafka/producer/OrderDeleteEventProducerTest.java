package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.OrderMother;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.DeleteOrder;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import com.nbusto.spring.boot.poc.spring.kafka.KafkaProperties;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = SpringBootTestsApplication.class)
@DirtiesContext
@Testcontainers
class OrderDeleteEventProducerTest {

  @Container
  static final ConfluentKafkaContainer KAFKA_CONTAINER =
    new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.5"));

  private static Map<String, Object> CONSUMER_PROPS;

  @Autowired
  private KafkaProperties kafkaProperties;

  @Autowired
  private OrderDeleteEventProducer sut;

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

  private Consumer<String, DeleteOrder> createConsumer() {
    final var consumerFactory = new DefaultKafkaConsumerFactory<String, DeleteOrder>(CONSUMER_PROPS);

    return consumerFactory.createConsumer();
  }

  @Test
  void given_a_valid_message_when_sent_then_is_registered() {
    // Given
    final var request = OrderMother.random();
    final var topic = kafkaProperties.topics().deleteTopic();
    final var consumer = createConsumer();

    // When
    sut.sendDeleteEvent(request);

    // Then
    consumer.subscribe(Collections.singletonList(topic));

    final var capturedEvent = KafkaTestUtils.getSingleRecord(
      consumer,
      topic,
      Duration.ofSeconds(3)
    );

    then(capturedEvent)
      .isNotNull()
      .satisfies(event -> {
        then(event.key())
          .isNotNull()
          .isInstanceOfSatisfying(String.class, it -> then(it)
            .isNotNull()
            .matches(Pattern.compile("[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}"))
          );

        then(event.value())
          .isNotNull()
          .isInstanceOfSatisfying(DeleteOrder.class, it -> {

            then(it.getId())
              .isNotNull()
              .isEqualTo(request.id());

            then(it.getDeleteTime())
              .isNotNull()
              .isCloseTo(request.creationTime().toLocalDateTime(), within(1, ChronoUnit.SECONDS));
          });
      });
  }
}
