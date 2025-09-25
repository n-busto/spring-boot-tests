package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.OrderMother;
import com.nbusto.spring.boot.poc.infra.kafka.KafkaTestContext;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.CreateOrderEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;

class OrderCreationEventProducerTest extends KafkaTestContext {

  @Autowired
  private OrderCreationEventProducer sut;

  @Test
  void given_a_valid_message_when_sent_then_is_registered() {
    // Given
    final var request = OrderMother.random();

    // When
    sut.sendCreationEvent(request);

    // Then
    final var capturedEvent = consumer.consumeMessage("com.nbusto.spring.boot.poc.creation.0");

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
          .isInstanceOfSatisfying(CreateOrderEvent.class, it -> {

            then(it.getId())
              .isNotNull()
              .isEqualTo(request.id());

            then(it.getUuid())
              .isNotNull()
              .isEqualTo(request.uuid().toString());

            then(it.getCreationTime())
              .isNotNull()
              .isCloseTo(request.creationTime().toLocalDateTime(), within(1, ChronoUnit.SECONDS));
          });
      });
  }
}
