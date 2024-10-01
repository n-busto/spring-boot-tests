package com.nbusto.spring.boot.poc.infra.status.controller;

import com.nbusto.spring.boot.poc.application.status.usecase.HealthCheckUseCase;
import com.nbusto.spring.boot.poc.domain.status.Health;
import com.nbusto.spring.boot.poc.infra.common.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.infra.common.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ControllerTest(controllers = HealthCheckController.class)
class HealthCheckControllerTest extends BaseControllerTest {

  @MockBean
  private HealthCheckUseCase useCase;

  @Test
  void given_status_down_when_requested_return_status_down() throws Exception {
    // Given
    BDDMockito.given(useCase.getHealth()).willReturn(new Health("DOWN"));

    // Expect
    performAction(get("/healthcheck"), 200)
      .andExpect(content().json("{ \"status\": \"DOWN\" }"));
  }

  @Test
  void given_status_up_when_requested_return_status_down() throws Exception {
    // Given
    BDDMockito.given(useCase.getHealth()).willReturn(new Health("UP"));

    // Expect
    performAction(get("/healthcheck"), 200)
      .andExpect(content().json("{ \"status\": \"UP\" }"));
  }
}
