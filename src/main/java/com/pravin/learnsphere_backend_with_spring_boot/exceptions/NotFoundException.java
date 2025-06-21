package com.pravin.learnsphere_backend_with_spring_boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
  public NotFoundException(String msg) {
    super(msg);
  }
}

