package com.hedbanz.sockets.transfer;

import com.fasterxml.jackson.annotation.JsonSetter;

public class GameOverDto {
    private Boolean isGameOver;

    public GameOverDto(Boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    @JsonSetter("isGameOver")
    public void setIsGameOver(Boolean gameOver) {
        isGameOver = gameOver;
    }

    public Boolean isGameOver() {
        return isGameOver;
    }
}
