package com.nbusto.spring.boot.poc.arch;

import com.nbusto.spring.boot.poc.application.ApplicationTest;
import com.nbusto.spring.boot.poc.infra.annotations.ControllerTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
  packages = "com.nbusto.spring.boot.poc",
  importOptions = ImportOption.OnlyIncludeTests.class)
class TestArchitectureRules {

  @ArchTest
  public static final ArchRule CLASSES_MAY_HAVE_TEST_ENDING = classes()
    .that().resideOutsideOfPackage("..arch..")
    .and().areNotNestedClasses()
    .should().haveSimpleNameEndingWith("Test");

  @ArchTest
  public static final ArchRule CONTROLLER_TEST_MUST_NOT_WAKE_FULL_APP =
    classes()
      .that().resideInAnyPackage("..infra..controller..")
      .should().beAnnotatedWith(ControllerTest.class);

  @ArchTest
  public static final ArchRule ADAPTER_TEST_MUST_NOT_WAKE_FULL_APP =
    classes()
      .that().resideInAnyPackage("..infra..controller..")
      .and().areNotInterfaces()
      .should().beAnnotatedWith(ControllerTest.class);

  @ArchTest
  public static final ArchRule APPLICATION_TEST_MUST_BE_UNIT =
    classes()
      .that().resideInAnyPackage("..application..")
      .and().areNotInterfaces()
      .should().beAnnotatedWith(ApplicationTest.class);


}
