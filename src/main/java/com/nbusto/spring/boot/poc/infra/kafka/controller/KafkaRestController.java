package com.nbusto.spring.boot.poc.infra.kafka.controller;


import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.producer.OrderCreationEventProducer;
import com.nbusto.spring.boot.poc.infra.kafka.request.OrderCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/injection/kafka/order")
@RequiredArgsConstructor
public class KafkaRestController {

  private final OrderCreationEventProducer orderCreationEventProducer;

  @PostMapping
  public ResponseEntity<Void> createOrder(
    @RequestBody OrderCreationRequest request
  ) {
    Order order = creationRequestAsDomain(request);
    orderCreationEventProducer.sendCreationEvent(order);

    return ResponseEntity.accepted().build();
  }

  private Order creationRequestAsDomain(OrderCreationRequest request) {
    return new Order(
      request.id(),
      OffsetDateTime.now(),
      UUID.randomUUID(),
      null
    );
  }
}
