package com.nbusto.spring.boot.poc.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
  packages = "com.nbusto.spring.boot.poc",
  importOptions = ImportOption.DoNotIncludeTests.class)
class CommonArchitectureRulesTest {

  @ArchTest
  public static final ArchRule CLASSES_MAY_HAVE_DESCRIPTIVE_NAMES = classes()
    .should().haveSimpleNameNotEndingWith("Impl")
    .andShould().haveSimpleNameNotEndingWith("I")
    .andShould().haveSimpleNameNotEndingWith("Test");

  @ArchTest
  public static final ArchRule SPRING_CLASSES_MUST_BE_IN_THEIR_PACKAGE = classes()
    .that().areAnnotatedWith(SpringBootApplication.class)
    .or().areAnnotatedWith(Configuration.class)
    .should()
    .resideInAnyPackage("*..spring.boot.poc.spring..");

  @ArchTest
  public static final ArchRule SPRING_ANNOTATIONS_MAY_NOT_BE_USED_IN_DOMAIN_OR_APP = classes()
    .that().areAnnotatedWith(RestController.class)
    .or().areAnnotatedWith(Controller.class)
    .or().areAnnotatedWith(Service.class)
    .should()
    .resideOutsideOfPackage("*..(domain|application)..*");
}
