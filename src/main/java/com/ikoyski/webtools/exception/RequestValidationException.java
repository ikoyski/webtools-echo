package com.ikoyski.webtools.exception;

public class RequestValidationException extends RuntimeException {

  public RequestValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestValidationException(String message) {
    super(message);
  }

}
