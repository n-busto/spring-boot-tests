package com.nbusto.spring.boot.poc.arch;

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
    .should().haveSimpleNameEndingWith("Test")
    .orShould().haveSimpleNameNotEndingWith("IntTest");
}
