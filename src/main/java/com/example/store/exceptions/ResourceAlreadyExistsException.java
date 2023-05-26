package com.example.store.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{

  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
