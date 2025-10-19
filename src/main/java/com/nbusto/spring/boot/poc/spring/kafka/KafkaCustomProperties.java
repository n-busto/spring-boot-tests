package com.nbusto.spring.boot.poc.spring.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.config.kafka")
public record KafkaCustomProperties(
  Topics topics
) {

  public record Topics(
    String creationTopic,
    String deleteTopic
  ) {
  }
}
