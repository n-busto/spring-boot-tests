package com.nbusto.spring.boot.poc.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.nbusto.spring.boot.poc.infra")
class InfraArchitectureRulesTest {

  @ArchTest
  public static final ArchRule TESTS_MUST_BE_ACCEPTANCE_TEST = classes()
    .should().haveSimpleNameNotEndingWith("Test");
}
