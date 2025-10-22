package com.nbusto.spring.boot.poc.infra.kafka;


import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.avro.AvroSchemaProvider;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@DirtiesContext
@Import(SchemaRegistryTest.TestConfig.class)
class SchemaRegistryTest extends KafkaTestContext {


  private static final String TOPIC = "test-";

  private static final String SUBJECT = "test-value";

  private static final String SCHEMA_STR = """
    {
      "namespace": "com.nbusto.spring.boot.poc.infra.kafka.SchemaRegistryTest",
      "type": "record",
      "name": "TestEvent",
      "fields": [
        {
          "name": "id",
          "type": "int"
        },
        {
          "name": "name",
          "type": "string"
        }
      ]
    }
    """;

  @Autowired
  private KafkaTemplate<String, TestEvent> template;

  @Autowired
  private SchemaRegistryClient schemaRegistryClient;

  @Test
  void given_valid_event_when_sending_then_does_not_throws_exception() throws RestClientException, IOException {

    // Given
    registerSchema();

    final var event = new TestEvent.TestEventBuilder()
      .id(1)
      .name("test")
      .build();

    // Expect
    Assertions.assertDoesNotThrow(() -> template.send(TOPIC, event));
  }

  @Test
  void given_not_valid_event_when_sending_then_throws_exception() throws RestClientException, IOException {

    // Given
    registerSchema();

    final var event = new TestEvent.TestEventBuilder().build();

    // Expect
    Assertions.assertThrows(Exception.class, () -> template.send(TOPIC, event));
  }

  private void registerSchema() throws IOException, RestClientException {
    final var parsedSchema = schemaRegistryClient.parseSchema(
      AvroSchema.TYPE,
      SCHEMA_STR,
      List.of());

    if (parsedSchema.isPresent()) {
      final var schema = parsedSchema.get();
      schemaRegistryClient.register(SUBJECT, schema, 1, 1);
    }
  }

  @TestConfiguration
  static class TestConfig<K, V> {

    @Autowired
    private KafkaProperties properties;

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
      return new MockSchemaRegistryClient(Collections.singletonList(new AvroSchemaProvider()));
    }

    @Bean
    public KafkaAvroSerializer kafkaJsonSchemaSerializer(final SchemaRegistryClient schemaRegistryClient) {
      return new KafkaAvroSerializer(schemaRegistryClient);
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
      return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(null));
    }

  }

  private record TestEvent(int id, String name) implements IndexedRecord {

    @Override
    public void put(int i, Object v) {
    }

    @Override
    public Object get(int i) {
      return switch (i) {
        case 0 -> id;
        case 1 -> name;
        default -> throw new IndexOutOfBoundsException("Invalid index: " + i);
      };
    }

    @Override
    public Schema getSchema() {
      return new Schema.Parser().parse(SCHEMA_STR);
    }

    public static class TestEventBuilder {
      private int id;
      private String name;

      public TestEventBuilder id(int id) {
        this.id = id;
        return this;
      }

      public TestEventBuilder name(String name) {
        this.name = name;
        return this;
      }

      public TestEvent build() {
        return new TestEvent(id, name);
      }
    }
  }
}
