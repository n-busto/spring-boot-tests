package com.nbusto.spring.boot.poc.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.nbusto.spring.boot.poc.domain")
class DomainArchitectureRulesTest {

  @ArchTest
  public static final ArchRule MUST_NOT_USE_INFRA = noClasses()
    .should().dependOnClassesThat()
    .resideInAPackage("com.nbusto.spring.boot.poc.(infra|application)..*");
}
