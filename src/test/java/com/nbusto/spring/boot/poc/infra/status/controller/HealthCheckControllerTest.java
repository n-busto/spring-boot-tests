package com.nbusto.spring.boot.poc.infra.status.controller;

import com.nbusto.spring.boot.poc.application.status.usecase.HealthCheckUseCase;
import com.nbusto.spring.boot.poc.domain.status.Health;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = HealthCheckController.class)
class HealthCheckControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private HealthCheckUseCase useCase;

  @Test
  void given_status_down_when_requested_return_status_down() throws Exception {
    // Given
    BDDMockito.given(useCase.getHealth()).willReturn(new Health("DOWN"));

    // Expect
    mockMvc.perform(get("/healthcheck")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json("{ \"status\": \"DOWN\" }"));
  }

  @Test
  void given_status_up_when_requested_return_status_down() throws Exception {
    // Given
    BDDMockito.given(useCase.getHealth()).willReturn(new Health("UP"));

    // Expect
    mockMvc.perform(get("/healthcheck")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json("{ \"status\": \"UP\" }"));
  }
}
