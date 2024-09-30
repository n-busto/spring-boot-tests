package com.nbusto.spring.boot.poc.infra.status.controller;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.nbusto.spring.boot.poc.application.status.usecase.HealthCheckUseCase;
import com.nbusto.spring.boot.poc.domain.status.Health;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = HealthCheckController.class)
class HealthCheckControllerTest {
  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private HealthCheckUseCase useCase;

  public OpenApiInteractionValidator createValidator() throws IOException {
    return OpenApiInteractionValidator
      .createForInlineApiSpecification(resourceLoader
        .getResource("classpath:static/test-contract.yaml")
        .getContentAsString(Charset.defaultCharset()))
      .build();
  }

  @Test
  void given_status_down_when_requested_return_status_down() throws Exception {
    // Given
    BDDMockito.given(useCase.getHealth()).willReturn(new Health("DOWN"));

    // Expect
    mockMvc.perform(get("/healthcheck"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json("{ \"status\": \"DOWN\" }"))
      .andExpect(openApi().isValid(createValidator()));
  }

  @Test
  void given_status_up_when_requested_return_status_down() throws Exception {
    // Given
    BDDMockito.given(useCase.getHealth()).willReturn(new Health("UP"));

    // Expect
    mockMvc.perform(get("/healthcheck"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json("{ \"status\": \"UP\" }"));
  }
}
