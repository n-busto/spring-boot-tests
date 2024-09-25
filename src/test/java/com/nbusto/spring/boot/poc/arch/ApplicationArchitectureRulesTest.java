package com.nbusto.spring.boot.poc.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.nbusto.spring.boot.poc.application")
class ApplicationArchitectureRulesTest {

  @ArchTest
  public static final ArchRule TESTS_MUST_BE_IT = classes()
    .should().haveSimpleNameNotEndingWith("It")
    .andShould().beAnnotatedWith(WebMvcTest.class);

  @ArchTest
  public static final ArchRule MUST_NOT_USE_INFRA = noClasses()
    .should().dependOnClassesThat()
    .resideInAPackage("com.nbusto.spring.boot.poc.infra..*");
}
