package com.nbusto.spring.boot.poc.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(
  packages = "com.nbusto.spring.boot.poc",
  importOptions = ImportOption.DoNotIncludeTests.class)
class CommonArchitectureRulesTest {

  @ArchTest
  public static final ArchRule CLASSES_MAY_HAVE_DESCRIPTIVE_NAMES = classes()
    .should().haveSimpleNameNotEndingWith("Impl")
    .andShould().haveSimpleNameNotEndingWith("I");

  @ArchTest
  public static final ArchRule CLASSES_MAY_BE_IN_BASE_PACKAGE = classes()
    .that().areNotAnnotatedWith(SpringBootApplication.class)
    .should()
    .resideInAnyPackage("*..(infra|application|domain)..*");
}
