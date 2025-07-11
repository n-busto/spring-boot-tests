package com.nbusto.spring.boot.poc.infra.kafka.producer;

import com.nbusto.spring.boot.poc.domain.kafka.OrderMother;
import com.nbusto.spring.boot.poc.infra.kafka.KafkaTestContext;
import com.nbusto.spring.boot.poc.infra.kafka.v1.dto.CreateOrder;
import com.nbusto.spring.boot.poc.spring.SpringBootTestsApplication;
import com.nbusto.spring.boot.poc.spring.kafka.KafkaProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = SpringBootTestsApplication.class)
class OrderCreationEventProducerTest extends KafkaTestContext {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private OrderCreationEventProducer sut;

    @Test
    void given_a_valid_message_when_sent_then_is_registered() {
        // Given
        final var request = OrderMother.random();
        final var topic = kafkaProperties.topics().creationTopic();
        final var consumer = createConsumer();

        // When
        sut.sendCreationEvent(request);

        // Then
        consumer.subscribe(Collections.singletonList(topic));

        final var capturedEvent = KafkaTestUtils.getSingleRecord(
                consumer,
                topic,
                Duration.ofSeconds(3)
        );

        then(capturedEvent)
                .isNotNull()
                .satisfies(event -> {
                    then(event.key())
                            .isNotNull()
                            .isInstanceOfSatisfying(String.class, it -> then(it)
                                    .isNotNull()
                                    .matches(Pattern.compile("[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}"))
                            );

                    then(event.value())
                            .isNotNull()
                            .isInstanceOfSatisfying(CreateOrder.class, it -> {

                                then(it.getId())
                                        .isNotNull()
                                        .isEqualTo(request.id());

                                then(it.getUuid())
                                        .isNotNull()
                                        .isEqualTo(request.uuid().toString());

                                then(it.getCreationTime())
                                        .isNotNull()
                                        .isCloseTo(request.creationTime().toLocalDateTime(), within(1, ChronoUnit.SECONDS));
                            });
                });
    }
}
