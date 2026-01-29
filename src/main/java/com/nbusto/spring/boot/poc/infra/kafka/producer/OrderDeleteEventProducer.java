package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.DeleteOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDeleteEventProducer {

  private final StreamBridge bridge;

  public void sendDeleteEvent(final Order order) {
    final var message = MessageBuilder
      .withPayload(orderToEvent(order))
      .setHeader(KafkaHeaders.KEY, order.uuid().toString())
      .build();

    bridge.send(
      "delete-out-0",
      message);
  }

  private DeleteOrderEvent orderToEvent(final Order order) {
    return DeleteOrderEvent.newBuilder()
      .setId(order.id())
      .setDeleteTime(order.deleteTime().toLocalDateTime())
      .build();
  }
}
