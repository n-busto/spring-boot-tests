package com.nbusto.spring.boot.poc.domain.kafka;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Order(
  String id,
  OffsetDateTime creationTime,
  UUID uuid,
  OffsetDateTime deleteTime
) {
}
