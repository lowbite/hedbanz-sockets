package com.hedbanz.sockets.transfer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hedbanz.sockets.deserializer.WordDTODeserializer;

@JsonDeserialize(using = WordDTODeserializer.class)
public class WordDto {
    private Long roomId;
    private Long senderId;
    private String word;
    private Long wordReceiverId;
    private String securityToken;

    private WordDto(Long roomId, Long senderId, String word, Long wordReceiverId, String securityToken) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.word = word;
        this.wordReceiverId = wordReceiverId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getWordReceiverId() {
        return wordReceiverId;
    }

    public void setWordReceiverId(Long wordReceiverId) {
        this.wordReceiverId = wordReceiverId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
