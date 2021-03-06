package com.hedbanz.sockets.transfer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hedbanz.sockets.deserializer.UserToRoomDeserializer;

import javax.validation.constraints.NotNull;

@JsonDeserialize(using = UserToRoomDeserializer.class)
public class UserToRoomDto {
    private Long userId;
    private Long roomId;
    private String password;
    private String securityToken;

    public UserToRoomDto(){

    }

    public UserToRoomDto(Long userId, Long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    public UserToRoomDto(Long userId, Long roomId, String password, String securityToken) {
        this.userId = userId;
        this.roomId = roomId;
        this.password = password;
        this.securityToken = securityToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserToRoomDto that = (UserToRoomDto) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roomId != null ? !roomId.equals(that.roomId) : that.roomId != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roomId != null ? roomId.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public static class Builder {
        private Long userId;
        private Long roomId;
        private String password;
        private String securityToken;

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSecurityToken(String securityToken){
            this.securityToken = securityToken;
            return this;
        }

        public UserToRoomDto build() {
            return new UserToRoomDto(userId, roomId, password, securityToken);
        }
    }
}
