package com.nbusto.spring.boot.poc.infra.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Collections;
import java.util.Map;

public class KafkaTestConsumer {

  private final Map<String, Object> properties;

  public KafkaTestConsumer(
    final String bootstrapServers,
    final String schemaRegistryUrl) {

    this.properties = KafkaTestUtils.consumerProps(bootstrapServers, "test-group", "true");

    properties.put("schema.registry.url", schemaRegistryUrl);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
  }

  public <T> ConsumerRecord<String, T> consumeMessage(final String topic) {
    final var consumer = new DefaultKafkaConsumerFactory<String, T>(properties).createConsumer();

    consumer.subscribe(Collections.singletonList(topic));

    return KafkaTestUtils.getSingleRecord(consumer, topic);
  }
}
