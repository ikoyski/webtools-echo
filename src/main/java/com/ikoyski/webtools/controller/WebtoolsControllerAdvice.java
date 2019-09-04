package com.ikoyski.webtools.controller;

import java.net.ConnectException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.ikoyski.webtools.dto.ErrorResponse;
import com.ikoyski.webtools.exception.RequestValidationException;
import com.ikoyski.webtools.exception.WebtoolsConfigurationException;

@ControllerAdvice
public class WebtoolsControllerAdvice {

  public static final String JSON_PARSE_ERROR = "Malformed Request - Please make sure that the request is valid json"
      + " or that field datatypes are correct.";
  public static final String CONNECTION_TIMEOUT_ERROR = "Something went wrong, please try again later.";

  public WebtoolsControllerAdvice() {
    super();
  }

  private ErrorResponse handleError(String message) {
    return ErrorResponse.builder().message(message).build();    
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(RequestValidationException.class)
  public @ResponseBody ErrorResponse requestValidationException(Throwable exception) {
    return handleError(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public @ResponseBody ErrorResponse httpMessageNotReadableException(Throwable exception) {
    return handleError(JSON_PARSE_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(NumberFormatException.class)
  public @ResponseBody ErrorResponse numberFormatException(Throwable exception) {
    return handleError(JSON_PARSE_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidFormatException.class)
  public @ResponseBody ErrorResponse invalidFormatException(Throwable exception) {
    return handleError(JSON_PARSE_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @ResponseBody ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException exception) {
    return handleError(exception.getBindingResult().getFieldError().getDefaultMessage());
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResponseStatusException.class)
  public @ResponseBody ErrorResponse notFound(Throwable exception) {
    return handleError(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(WebtoolsConfigurationException.class)
  public @ResponseBody ErrorResponse invalidConfigurationException(Throwable exception) {
    return handleError(exception.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ConnectException.class)
  public @ResponseBody ErrorResponse connectionException(Throwable exception) {
    return handleError(CONNECTION_TIMEOUT_ERROR);
  }

}
