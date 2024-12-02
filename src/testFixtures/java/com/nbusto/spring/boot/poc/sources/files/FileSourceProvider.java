package com.nbusto.spring.boot.poc.sources.files;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileSourceProvider extends AnnotationBasedArgumentsProvider<FileSource> {

  private List<File> retrieveFiles(String path) {
    try (Stream<Path> paths = Files.walk(Paths.get(path), 1)) {
      return paths.filter(Files::isRegularFile)
        .map(Path::toFile)
        .toList();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected Stream<? extends Arguments> provideArguments(ExtensionContext context, FileSource annotation) {
    return retrieveFiles(annotation.basePath() + annotation.value())
      .stream()
      .map(it -> {
        try {
          return FileUtils.readFileToString(it, Charset.defaultCharset());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      })
      .map(Arguments::of);
  }
}
