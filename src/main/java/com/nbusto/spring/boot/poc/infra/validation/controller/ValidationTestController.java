package com.nbusto.spring.boot.poc.infra.validation.controller;

import com.nbusto.spring.boot.poc.infra.validation.request.TestRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/validation")
public class ValidationTestController {

  @PostMapping
  public ResponseEntity<String> verifyValidation(@Valid @RequestBody TestRequest request) {
    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
    MethodArgumentNotValidException ex) {

    final var errors = ex.getBindingResult().getAllErrors()
      .stream()
      .map(it -> (FieldError) it)
      .collect(Collectors.toMap(
        FieldError::getField,
        it -> Optional.ofNullable(it.getDefaultMessage())
          .orElse("Unknown error")));

    return ResponseEntity.badRequest()
      .contentType(MediaType.APPLICATION_JSON)
      .body(errors);
  }
}
