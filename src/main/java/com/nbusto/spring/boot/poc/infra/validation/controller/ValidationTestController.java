package com.nbusto.spring.boot.poc.infra.validation.controller;

import com.nbusto.spring.boot.poc.infra.validation.request.TestRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation")
@RequiredArgsConstructor
public class ValidationTestController {

  @PostMapping
  public ResponseEntity<String> verifyValidation(@Valid @RequestBody TestRequest request) {
    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_JSON)
      .build();
  }
}
