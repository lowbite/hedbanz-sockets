package com.hedbanz.sockets.transfer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hedbanz.sockets.deserializer.ClientInfoDTODeserializer;

@JsonDeserialize(using = ClientInfoDTODeserializer.class)
public class ClientInfoDto {
    private Long roomId;
    private Long userId;
    private String securityToken;

    public ClientInfoDto() {
    }

    public ClientInfoDto(Long roomId, Long userId, String securityToken) {
        this.roomId = roomId;
        this.userId = userId;
        this.securityToken = securityToken;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
