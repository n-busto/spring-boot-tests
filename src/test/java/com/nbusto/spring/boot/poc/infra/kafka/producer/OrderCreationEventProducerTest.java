package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.OrderMother;
import com.nbusto.spring.boot.poc.infra.kafka.KafkaTestContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;

class OrderCreationEventProducerTest extends KafkaTestContext {

  @Autowired
  private OrderCreationEventProducer sut;

  @Test
  void given_a_valid_message_when_sent_then_must_not_throw_exception() {
    // Given
    final var request = OrderMother.random();

    // Then
    assertThatNoException().isThrownBy(() -> sut.sendCreationEvent(request));
  }

  @Test
  void given_a_valid_message_when_sent_then_must_produce_valid_message() {
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
          .isEqualTo(request.uuid().toString());

        then(event.value())
          .isNotNull()
          .hasFieldOrPropertyWithValue("id", request.id())
          .hasFieldOrPropertyWithValue("uuid", request.uuid().toString())
          .hasFieldOrPropertyWithValue("creationTime", request.creationTime());
      });
  }
}
