package com.hedbanz.sockets.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.hedbanz.sockets.transfer.PlayerDto;
import com.hedbanz.sockets.transfer.RoomDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomDeserializer extends JsonDeserializer<RoomDto> {
    @Override
    public RoomDto deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException{
        JsonNode node = p.getCodec().readTree(p);
        String name = node.get("name").asText();
        String password = node.get("password").asText();
        int maxPlayers = node.get("maxPlayers").asInt();
        long userId = node.get("userId").asLong();
        boolean isPrivate = !(password == null || password.isEmpty());
        List<PlayerDto> playerDtos = new ArrayList<>();
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId(userId);
        playerDtos.add(playerDto);

        RoomDto roomDto = new RoomDto();
        roomDto.setName(name);
        roomDto.setPassword(password);
        roomDto.setMaxPlayers(maxPlayers);
        roomDto.setIsPrivate(isPrivate);
        roomDto.setPlayers(playerDtos);
        return roomDto;
    }
}
