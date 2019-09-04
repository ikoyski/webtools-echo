package com.ikoyski.webtools.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

@Component
public final class RequestUtils {

  private RequestUtils() {
  }

  public static String asJsonString(Object obj) throws JsonProcessingException {
    return RequestUtils.buildObjectMapper().writeValueAsString(obj);
  }

  public static <T> T mergePatch(T t, String patch, Class<T> clazz) throws IOException, JsonPatchException {
    ObjectMapper mapper = RequestUtils.buildObjectMapper();
    JsonNode node = mapper.convertValue(t, JsonNode.class);
    JsonNode patchNode = mapper.readTree(patch);
    JsonMergePatch mergePatch = JsonMergePatch.fromJson(patchNode);
    node = mergePatch.apply(node);
    return mapper.treeToValue(node, clazz);
  }

  public static <T> T jsonStringToPojo(String json, Class<T> clazz) throws IOException {
    return RequestUtils.buildObjectMapper().readValue(json, clazz);
  }

  public static ObjectMapper buildObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper;
  }

}
