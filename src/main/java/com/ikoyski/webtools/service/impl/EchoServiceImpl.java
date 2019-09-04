package com.ikoyski.webtools.service.impl;

import org.springframework.stereotype.Component;

import com.ikoyski.webtools.service.EchoService;

@Component
public class EchoServiceImpl implements EchoService {

  @Override
  public String echo(String message) {
    return message;
  }

}
