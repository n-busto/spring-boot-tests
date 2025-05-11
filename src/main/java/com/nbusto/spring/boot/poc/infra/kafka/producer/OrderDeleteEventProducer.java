package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.DeleteOrder;
import com.nbusto.spring.boot.poc.spring.kafka.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
@Slf4j
public class OrderDeleteEventProducer {

  private final KafkaProperties kafkaProperties;

  private final KafkaTemplate<String, DeleteOrder> kafkaTemplate;

  public void sendDeleteEvent(Order order) {

    kafkaTemplate.send(
        kafkaProperties.topics().deleteTopic(),
        UUID.randomUUID().toString(),
        orderToEvent(order))
      .thenAccept(result -> log.info("Successfully send creation {}", result));
  }

  private DeleteOrder orderToEvent(Order order) {
    return DeleteOrder.newBuilder()
      .setId(order.id())
      .setDeleteTime(order.creationTime().toLocalDateTime())
      .build();
  }
}
