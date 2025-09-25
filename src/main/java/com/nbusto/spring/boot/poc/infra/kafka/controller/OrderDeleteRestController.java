package com.nbusto.spring.boot.poc.infra.kafka.controller;

import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.kafka.producer.OrderDeleteEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/injection/kafka/order/{id}")
@RequiredArgsConstructor
public class OrderDeleteRestController {

  private final OrderDeleteEventProducer orderDeleteEventProducer;

  @DeleteMapping
  public ResponseEntity<Void> deleteOrder(
    @PathVariable final String id
  ) {
    orderDeleteEventProducer.sendDeleteEvent(createOrder(id));

    return ResponseEntity.accepted().build();
  }

  private Order createOrder(final String id) {
    return new Order(id, null, null, OffsetDateTime.now());
  }

}
