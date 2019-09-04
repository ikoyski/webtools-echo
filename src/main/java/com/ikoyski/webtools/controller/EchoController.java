package com.ikoyski.webtools.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.ikoyski.webtools.dto.EchoRequest;
import com.ikoyski.webtools.dto.EchoResponse;
import com.ikoyski.webtools.service.EchoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EchoController {

  private final EchoService echoService;

  public EchoController(EchoService echoService) {
    super();
    this.echoService = echoService;
  }

  @PostMapping("/echo")
  public ResponseEntity<EchoResponse> echo(@Valid @RequestBody EchoRequest echoRequest, 
      WebRequest requestAttributes) {

    LOGGER.debug("Echo request received {}", echoRequest);
    
    if (echoRequest.getMessage() == null) {
      LOGGER.error("No message was sent");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    EchoResponse echoResponse = EchoResponse.builder().message(echoService.echo(
        echoRequest.getMessage())).build();

    LOGGER.debug("Echo response returned {}", echoResponse);

    return new ResponseEntity<>(echoResponse, HttpStatus.OK);
    
  }

}
