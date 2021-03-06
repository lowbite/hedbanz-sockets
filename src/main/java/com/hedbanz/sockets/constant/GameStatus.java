package com.hedbanz.sockets.constant;

public enum GameStatus {
    WAITING_FOR_PLAYERS(1),
    SETTING_WORDS(2),
    GUESSING_WORDS(3),
    GAME_OVER(4);

    private int code;

    GameStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
