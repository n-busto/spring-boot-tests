package com.nbusto.spring.boot.poc.infra.injection.controller;

import com.nbusto.spring.boot.poc.application.injection.usecase.BeanInjectionUseCase;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.nbusto.spring.boot.poc.application.injection.StringListMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = BeanInjectionController.class)
class BeanInjectionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BeanInjectionUseCase useCase;

  @Test
  void given_a_bean_list_then_data_is_returned_right()
    throws Exception {
    // Given
    final var randomBeanNameList = StringListMother.random();

    given(useCase.getBeanNames())
      .willReturn(randomBeanNameList);

    // Expect
    mockMvc.perform(get("/injection"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.numberOfBeans").exists())
      .andExpect(jsonPath("$.numberOfBeans").isNumber())
      .andExpect(jsonPath("$.numberOfBeans", is(randomBeanNameList.size())))
      .andExpect(jsonPath("$.beanNames").exists())
      .andExpect(jsonPath("$.beanNames").isNotEmpty())
      .andExpect(jsonPath("$.beanNames").value(randomBeanNameList));
  }

  @Test
  void given_an_empty_bean_list_then_right_information_is_returned_right()
    throws Exception {
    // Given
    given(useCase.getBeanNames())
      .willReturn(List.of());

    // Expect
    mockMvc.perform(get("/injection"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.numberOfBeans").exists())
      .andExpect(jsonPath("$.numberOfBeans").isNumber())
      .andExpect(jsonPath("$.numberOfBeans", is(0)))
      .andExpect(jsonPath("$.beanNames").exists())
      .andExpect(jsonPath("$.beanNames").isEmpty());
  }
}