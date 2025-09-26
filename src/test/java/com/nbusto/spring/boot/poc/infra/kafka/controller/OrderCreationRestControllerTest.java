package com.nbusto.spring.boot.poc.infra.kafka.controller;

import com.nbusto.spring.boot.poc.application.injection.StringMother;
import com.nbusto.spring.boot.poc.domain.kafka.Order;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.infra.kafka.producer.OrderCreationEventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = OrderCreationRestController.class)
class OrderCreationRestControllerTest {

  public static final String ENDPOINT_URI = "/injection/kafka/order";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OrderCreationEventProducer producer;

  @Captor
  private ArgumentCaptor<Order> orderCaptor;

  @Test
  void given_a_right_request_when_endpoint_is_invoked_then_response_is_accepted() throws Exception {
    // Given
    final var id = StringMother.random();

    final var request = """
      {
        "id": "%s"
      }
      """.formatted(id);

    willDoNothing()
      .given(producer)
      .sendCreationEvent(orderCaptor.capture());

    // Expect
    mockMvc.perform(post(ENDPOINT_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(request))
      .andExpect(status().isAccepted());

    then(orderCaptor.getValue())
      .isNotNull()
      .satisfies(it -> {
        then(it.id())
          .isNotNull()
          .isNotBlank()
          .isEqualTo(id);

        then(it.creationTime())
          .isNotNull()
          .isCloseTo(OffsetDateTime.now(), within(1, ChronoUnit.SECONDS));

        then(it.uuid())
          .isNotNull();

        then(it.deleteTime())
          .isNull();
      });
  }
}
