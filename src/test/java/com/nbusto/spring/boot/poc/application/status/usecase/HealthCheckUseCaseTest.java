package com.nbusto.spring.boot.poc.application.status.usecase;

import com.nbusto.spring.boot.poc.application.ApplicationTest;
import com.nbusto.spring.boot.poc.application.status.port.HealthPort;
import com.nbusto.spring.boot.poc.domain.status.Health;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ApplicationTest
class HealthCheckUseCaseTest {

  @Mock
  private HealthPort port;

  @InjectMocks
  private HealthCheckUseCase useCase;

  @Test
  void given_status_down_then_must_return_down() {
    // Given
    given(port.getHealth())
      .willReturn(new Health("DOWN"));

    // Expect
    then(useCase.getHealth())
      .isNotNull()
      .matches(it -> it.status().equals("DOWN"));
  }

  @Test
  void given_status_up_then_must_return_down() {
    // Given
    given(port.getHealth())
      .willReturn(new Health("UP"));

    // Expect
    then(useCase.getHealth())
      .isNotNull()
      .matches(it -> it.status().equals("UP"));
  }
}
