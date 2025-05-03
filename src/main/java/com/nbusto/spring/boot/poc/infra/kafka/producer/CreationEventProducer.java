package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.infra.injection.dto.CreateOrder;
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
public class CreationEventProducer {

  private final KafkaProperties kafkaProperties;

  private final KafkaTemplate<String, CreateOrder> kafkaTemplate;

  public void sendCreationEvent(CreateOrder order) {

    kafkaTemplate.send(
        kafkaProperties.topics().creationTopic(),
        UUID.randomUUID().toString(),
        order)
      .thenAccept(result -> {
        log.info("Successfully send creation event for order {}", result);
      });
  }
}
