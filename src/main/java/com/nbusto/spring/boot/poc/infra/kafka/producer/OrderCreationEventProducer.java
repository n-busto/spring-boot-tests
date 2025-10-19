package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.CreateOrderEvent;
import com.nbusto.spring.boot.poc.spring.kafka.KafkaCustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaCustomProperties.class)
@Slf4j
public class OrderCreationEventProducer {

  private final KafkaCustomProperties kafkaCustomProperties;

  private final KafkaTemplate<String, CreateOrderEvent> kafkaTemplate;

  public void sendCreationEvent(Order order) {

    kafkaTemplate.send(
        kafkaCustomProperties.topics().creationTopic(),
        UUID.randomUUID().toString(),
        orderToEvent(order))
      .thenAccept(result -> log.info("Successfully send creation {}", result));
  }

  private CreateOrderEvent orderToEvent(Order order) {
    return CreateOrderEvent.newBuilder()
      .setUuid(order.uuid().toString())
      .setId(order.id())
      .setCreationTime(order.creationTime().toLocalDateTime())
      .build();
  }
}
