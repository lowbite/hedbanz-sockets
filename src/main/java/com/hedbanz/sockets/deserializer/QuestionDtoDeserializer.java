package com.hedbanz.sockets.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.hedbanz.sockets.transfer.QuestionDto;

import java.io.IOException;

public class QuestionDtoDeserializer extends JsonDeserializer<QuestionDto> {
    @Override
    public QuestionDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Gson gson = new Gson();
        String nodeString = node.asText();
        if(nodeString == null || nodeString.isEmpty()){
            nodeString = node.toString();
        }
        return gson.fromJson(nodeString, QuestionDto.class);
    }
}
