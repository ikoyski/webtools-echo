package com.ikoyski.webtools.junit5.controller;

import static com.ikoyski.webtools.service.RequestUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.ikoyski.webtools.controller.WebtoolsControllerAdvice;
import com.ikoyski.webtools.dto.EchoRequest;
import com.ikoyski.webtools.dto.EchoResponse;
import com.ikoyski.webtools.service.EchoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EchoControllerTest {
  
  private static final String MESSAGE = "This is a test message 1";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EchoService echoService;

  // Mocked Bean containers
  private EchoRequest echoRequest;
  
  @BeforeEach
  void initializeMockedBeans() {
    echoRequest = EchoRequest.builder().message(MESSAGE).build();
  }

  @Test
  @DisplayName("POST /api/v1/echo - Success")
  void testEchoHappyPath() throws Exception {

    // mock the service
    doReturn(MESSAGE).when(echoService).echo(MESSAGE);
    
    // expected Valid Response
    EchoResponse expectedResponse = EchoResponse.builder().message(MESSAGE).build();

    // mock the endpoint
    mockMvc
        .perform(post("/api/v1/echo").content(asJsonString(echoRequest))
            .contentType(MediaType.APPLICATION_JSON))

        // validates response code && content type
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

        // validates body fields
        .andExpect(jsonPath("$.message", is(expectedResponse.getMessage()))); 
  }
  
  @Test
  @DisplayName("POST /api/v1/echo - Message is null")
  void testEchoMessageIsNull() throws Exception {
    
    echoRequest.setMessage(null);

    // mock the endpoint
    mockMvc
        .perform(post("/api/v1/echo").content(asJsonString(echoRequest))
            .contentType(MediaType.APPLICATION_JSON))

        // validates response code && content type
        .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

        // validates body fields
        .andExpect(jsonPath("$.message", is("Message must not be null"))); 
  }
  
  @Test
  @DisplayName("POST /api/v1/echo - Body is not json")
  void testEchoBodyIsNotJson() throws Exception {
    
    // mock the endpoint
    mockMvc
        .perform(post("/api/v1/echo").content("").contentType(MediaType.APPLICATION_JSON))

        // validates response code && content type
        .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

        // validates body fields
        .andExpect(jsonPath("$.message", is(WebtoolsControllerAdvice.JSON_PARSE_ERROR))); 
  }

}
