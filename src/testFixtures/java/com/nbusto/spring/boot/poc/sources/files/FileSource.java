package com.nbusto.spring.boot.poc.sources.files;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retrieves all the files in the specific folder and returns them
 * as String parameter
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(FileSourceProvider.class)
public @interface FileSource {

  /**
   * The path to the folder containing files.
   */
  String value();

  /**
   * Base path where we will find the files
   */
  String basePath() default "src/test/resources/";
}
