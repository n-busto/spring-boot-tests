package com.nbusto.spring.boot.poc.infra.kafka.controller;

import com.nbusto.spring.boot.poc.application.injection.StringMother;
import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.infra.kafka.producer.OrderDeleteEventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = OrderDeleteRestController.class)
class OrderDeleteRestControllerTest {

  public static final String ENDPOINT_URI = "/injection/kafka/order/{id}";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OrderDeleteEventProducer producer;

  @Captor
  private ArgumentCaptor<Order> orderCaptor;

  @Test
  void given_a_right_request_when_endpoint_is_invoked_then_response_is_accepted() throws Exception {
    // Given
    final var id = StringMother.random();

    willDoNothing()
      .given(producer)
      .sendDeleteEvent(orderCaptor.capture());

    // Expect
    mockMvc.perform(delete(ENDPOINT_URI, id))
      .andExpect(status().isAccepted());

    then(orderCaptor.getValue())
      .isNotNull()
      .satisfies(it -> {
        then(it.id())
          .isNotNull()
          .isNotBlank()
          .isEqualTo(id);

        then(it.creationTime())
          .isNull();

        then(it.uuid())
          .isNull();

        then(it.deleteTime())
          .isNotNull()
          .isCloseTo(OffsetDateTime.now(), within(1, ChronoUnit.SECONDS));
      });
  }
}
