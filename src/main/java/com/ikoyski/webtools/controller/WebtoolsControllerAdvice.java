package com.ikoyski.webtools.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ikoyski.webtools.dto.ErrorResponse;

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
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public @ResponseBody ErrorResponse httpMessageNotReadableException(Throwable exception) {
    return handleError(JSON_PARSE_ERROR);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @ResponseBody ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException exception) {
    return handleError(exception.getBindingResult().getFieldError().getDefaultMessage());
  }

}
