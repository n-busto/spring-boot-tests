package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.DeleteOrderEvent;
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
public class OrderDeleteEventProducer {

  private final KafkaCustomProperties kafkaCustomProperties;

  private final KafkaTemplate<String, DeleteOrderEvent> kafkaTemplate;

  public void sendDeleteEvent(final Order order) {

    kafkaTemplate.send(
        kafkaCustomProperties.topics().deleteTopic(),
        UUID.randomUUID().toString(),
        orderToEvent(order))
      .thenAccept(result -> log.info("Successfully send delete {}", result));
  }

  private DeleteOrderEvent orderToEvent(final Order order) {
    return DeleteOrderEvent.newBuilder()
      .setId(order.id())
      .setDeleteTime(order.deleteTime().toLocalDateTime())
      .build();
  }
}
