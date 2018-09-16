package com.hedbanz.sockets.constant;

import org.springframework.beans.factory.annotation.Value;

public class RequestsURI {
    public static final String HOSTNAME = "http://localhost:8085";

    public static final String GET_PLAYER_URI = HOSTNAME + "/rooms/%d/player/%d";
    public static final String GET_PLAYERS_URI = HOSTNAME + "/rooms/%d/players";
    public static final String GET_ROOM_URI = HOSTNAME +  "/rooms/%d";
    public static final String JOIN_USER_TO_ROOM_URI = HOSTNAME + "/rooms/join-user";
    public static final String LEAVE_USER_FROM_ROOM_URI = HOSTNAME + "/rooms/leave";
    public static final String START_GAME_URI = HOSTNAME + "/game/start/room/%d";
    public static final String RESTART_GAME_URI = HOSTNAME + "/game/restart/room/%d/user/%d";
    public static final String START_GUESSING_URI = HOSTNAME + "/game/start-guessing/room/%d";
    public static final String SET_WORD_URI = HOSTNAME + "/game/player/word";
    public static final String ADD_QUESTION_URI = HOSTNAME + "/game/player/question";
    public static final String ADD_VOTE_URI = HOSTNAME + "/game/player/vote";
    public static final String CHECK_QUESTIONER_WIN_URI = HOSTNAME + "/game/room/%d/check-questioner-win";
    public static final String CHECK_GAME_OVER_URI = HOSTNAME + "/game/room/%d/is-game-over";
    public static final String GET_NEXT_GUESSING_PLAYER = HOSTNAME + "/game/room/%d/next-player";
    public static final String GET_SET_WORD_DTOS_URI = HOSTNAME + "/rooms/%d/events/set-word-entity";
    public static final String ADD_USER_MESSAGE_URI = HOSTNAME + "/rooms/messages/add";
    public static final String GET_USER_URI = HOSTNAME + "/user/%d";
    public static final String FCM_PUSH_AFK_WARNING_URI = HOSTNAME + "/fcm/user/%d/send/afk-warning";
    public static final String FCM_PUSH_USER_KICKED_URI = HOSTNAME + "/fcm/user/%d/send/kicked";
    public static final String PLAYER_RECONNECT_URI = HOSTNAME + "/socket-messaging/reconnect/user/%d/room/%d";
    public static final String PLAYER_DISCONNECT_URI = HOSTNAME + "/socket-messaging/disconnect/user/%d/room/%d";
    public static final String CHECK_LOGIN_URI = HOSTNAME + "/socket-messaging/login-availability";
    public static final String ADD_WAITING_FOR_PLAYERS_MESSAGE = HOSTNAME + "/rooms/%d/messages/waiting-players";
    public static final String GET_LAST_QUESTION = HOSTNAME + "/rooms/%d/messages/last-question";
    public static final String GET_ADVERTISE_TYPE = HOSTNAME + "/advertise/type";
    public static final String GET_ADVERTISE_RATE = HOSTNAME + "/advertise/rate";

    public static final String SET_PLAYER_WIN_URI = HOSTNAME + "/game/room/%d/player-win";
    public static final String GET_QUESTIONER_URI = HOSTNAME + "/rooms/%d/messages/question/%d/questioner";
    public static final String DELETE_EMPTY_QUESTIONS = HOSTNAME + "/rooms/%d/messages/user/%d/empty-question";
}
