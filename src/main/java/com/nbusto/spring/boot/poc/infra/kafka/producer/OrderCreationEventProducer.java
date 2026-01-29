package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.CreateOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreationEventProducer {

  private final StreamBridge bridge;

  public void sendCreationEvent(Order order) {

    final var success = bridge.send(
      "creation-out-0",
      orderToEvent(order));

    if (!success) {
      throw new RuntimeException("Unable to send Order Event");
    }
  }

  private CreateOrderEvent orderToEvent(Order order) {
    return CreateOrderEvent.newBuilder()
      .setUuid(order.uuid().toString())
      .setId(order.id())
      .setCreationTime(order.creationTime().toLocalDateTime())
      .build();
  }
}
