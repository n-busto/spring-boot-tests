package com.nbusto.spring.boot.poc.application.injection.usecase;

import com.nbusto.spring.boot.poc.application.ApplicationTest;
import com.nbusto.spring.boot.poc.application.injection.StringMother;
import com.nbusto.spring.boot.poc.application.injection.port.BeanPort;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.BDDAssertions.then;

@ApplicationTest
public class BeanInjectionUseCaseTest {
  @Spy
  private List<BeanPort> beanPortList;

  @InjectMocks
  private BeanInjectionUseCase sut;

  @Test
  void given_no_beans_empty_is_returned() {
    // When
    BDDMockito.given(beanPortList.stream())
      .willReturn(Stream.of());

    // Expect
    then(sut.getBeanNames())
      .isNotNull()
      .isEmpty();
  }

  @Test
  void given_some_beans_it_returns_their_names() {
    // Given
    final var beanNames = StringMother.randomList();

    // When
    BDDMockito.given(beanPortList.stream())
      .willReturn(beanNames.stream().map(it -> () -> it));

    // Then
    then(sut.getBeanNames())
      .isNotNull()
      .isNotEmpty()
      .containsAll(beanNames);
  }
}
