package com.hedbanz.sockets.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hedbanz.sockets.constant.*;
import com.hedbanz.sockets.error.NotFoundError;
import com.hedbanz.sockets.error.RoomError;
import com.hedbanz.sockets.error.UserError;
import com.hedbanz.sockets.exception.ApiException;
import com.hedbanz.sockets.exception.ExceptionFactory;
import com.hedbanz.sockets.service.GameService;
import com.hedbanz.sockets.service.PlayerService;
import com.hedbanz.sockets.service.RoomService;
import com.hedbanz.sockets.transfer.*;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.xml.soap.Text;
import java.util.*;

import static com.hedbanz.sockets.constant.GameStatus.GUESSING_WORDS;
import static com.hedbanz.sockets.constant.GameStatus.SETTING_WORDS;
import static com.hedbanz.sockets.constant.GameStatus.WAITING_FOR_PLAYERS;
import static com.hedbanz.sockets.constant.PlayerStatus.ACTIVE;
import static com.hedbanz.sockets.constant.PlayerStatus.AFK;
import static com.hedbanz.sockets.constant.SocketEvents.*;

@Component
public class RoomEventHandler {
    private final Logger log = LoggerFactory.getLogger("RoomEventListener");

    private static final String USER_ID_FIELD = "userId";
    private static final String ROOM_ID_FIELD = "roomId";
    private static final String SECURITY_TOKEN_FIELD = "securityToken";
    private final double PASS_POLL_PERCENT_VALUE = 0.8;

    private final GameService gameService;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final SocketIONamespace socketIONamespace;

    @Autowired
    public RoomEventHandler(GameService gameService, RoomService roomService, PlayerService playerService, SocketIOServer server) {
        this.socketIONamespace = server.addNamespace("/game");
        this.socketIONamespace.addConnectListener(onConnected());
        this.socketIONamespace.addDisconnectListener(onDisconnected());
        this.socketIONamespace.addEventListener(JOIN_ROOM_EVENT, UserToRoomDto.class, joinUserToRoom());
        this.socketIONamespace.addEventListener(LEAVE_ROOM_EVENT, UserToRoomDto.class, leaveUserFromRoom());
        this.socketIONamespace.addEventListener(CLIENT_TYPING_EVENT, UserToRoomDto.class, userStartTyping());
        this.socketIONamespace.addEventListener(CLIENT_STOP_TYPING_EVENT, UserToRoomDto.class, userStopTyping());
        this.socketIONamespace.addEventListener(CLIENT_MESSAGE_EVENT, MessageDto.class, sendUserMessage());
        this.socketIONamespace.addEventListener(CLIENT_SET_PLAYER_WORD_EVENT, WordDto.class, setWordToPlayer());
        this.socketIONamespace.addEventListener(CLIENT_CONNECT_INFO_EVENT, ClientInfoDto.class, setClientInfo());
        this.socketIONamespace.addEventListener(CLIENT_RESTORE_ROOM_EVENT, ClientInfoDto.class, restoreRoom());
        this.socketIONamespace.addEventListener(CLIENT_USER_GUESSING_EVENT, QuestionDto.class, addUserQuestion());
        this.socketIONamespace.addEventListener(CLIENT_USER_ANSWERING_EVENT, QuestionDto.class, addVoteToQuestion());
        this.socketIONamespace.addEventListener(CLIENT_RESTART_GAME, UserToRoomDto.class, restartGame());

        this.gameService = gameService;
        this.roomService = roomService;
        this.playerService = playerService;
    }

    private DataListener<UserToRoomDto> restartGame() {
        return ((client, data, ackSender) -> {
            RoomDto roomDto = gameService.restartGame(data.getRoomId(), data.getUserId(), client.get(SECURITY_TOKEN_FIELD));
            if (roomDto != null) {
                if (roomDto.getCurrentPlayersNumber().equals(roomDto.getMaxPlayers()))
                    startGame(roomDto, client.get(SECURITY_TOKEN_FIELD));
                else {
                    roomService.sendWaitingForPlayersMessage(roomDto.getId(), client.get(SECURITY_TOKEN_FIELD));
                    socketIONamespace.getRoomOperations(data.getRoomId().toString()).sendEvent(SERVER_WAITING_FOR_USERS, new UserToRoomDto());
                }
            }
        });
    }

    private void checkUser(ClientInfoDto clientInfo, SocketIOClient client, RoomDto room) {
        if (room.getGameStatus() == GameStatus.SETTING_WORDS.getCode())
            for (PlayerDto player : room.getPlayers()) {
                if (clientInfo.getUserId().equals(player.getUserId())) {
                    Map<SocketIOClient, PlayerDto> playerDtoMap = new HashMap<>();
                    playerDtoMap.put(client, player);
                    sendSetWordEvents(playerDtoMap, room, clientInfo.getSecurityToken());
                }
            }
    }

    private DataListener<ClientInfoDto> restoreRoom() {
        return (client, data, ackSender) -> {
            RoomDto roomDto = roomService.getRoom(data.getRoomId(), data.getSecurityToken());
            checkUser(data, client, roomDto);
            log.info("Set client info userId: " + data.getUserId() + " roomId: " + data.getRoomId());
            client.set(USER_ID_FIELD, data.getUserId());
            client.set(SECURITY_TOKEN_FIELD, data.getSecurityToken());
            if (data.getRoomId() != null) {
                UserToRoomDto userToRoom = new UserToRoomDto.Builder()
                        .setUserId(data.getUserId())
                        .setRoomId(data.getRoomId())
                        .build();
                log.info("Setting player status active");
                PlayerDto playerDto = playerService.reconnect(userToRoom, data.getSecurityToken());
                for (PlayerDto player : roomDto.getPlayers()) {
                    if (player.getUserId().equals(playerDto.getUserId())) {
                        player.setStatus(ACTIVE.getCode());
                        break;
                    }
                }
                client.sendEvent(SERVER_RESTORE_ROOM_EVENT, roomDto);
                client.set(ROOM_ID_FIELD, data.getRoomId());
                client.joinRoom(String.valueOf(data.getRoomId()));
                socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                        .sendEvent(SERVER_USER_RETURNED_EVENT, playerDto);
                log.info("Client restore room set userId: " + data.getUserId() + ", roomId: " + data.getRoomId());
            }
        };
    }

    private DataListener<ClientInfoDto> setClientInfo() {
        return (client, data, ackSender) -> {
            RoomDto roomDto = roomService.getRoom(data.getRoomId(), data.getSecurityToken());
            checkUser(data, client, roomDto);
            log.info("Set client info userId: " + data.getUserId() + " roomId: " + data.getRoomId());
            client.set(USER_ID_FIELD, data.getUserId());
            client.set(SECURITY_TOKEN_FIELD, data.getSecurityToken());
            if (data.getRoomId() != null) {
                client.set(ROOM_ID_FIELD, data.getRoomId());
                client.joinRoom(String.valueOf(data.getRoomId()));
                UserToRoomDto userToRoom = new UserToRoomDto.Builder()
                        .setUserId(data.getUserId())
                        .setRoomId(data.getRoomId())
                        .build();
                log.info("Setting player status active");
                PlayerDto playerDto = playerService.reconnect(userToRoom, data.getSecurityToken());
                socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId())).sendEvent(SERVER_USER_RETURNED_EVENT, playerDto);
                List<PlayerDto> playerDtos = playerService.getPlayersInRoom(userToRoom.getRoomId(), data.getSecurityToken());
                client.sendEvent(SERVER_PLAYERS_STATUS, playerDtos);
                log.info("Client reconnect set userId: " + data.getUserId() + ", roomId: " + data.getRoomId());
            }
        };
    }

    /**
     * This method removing user from room
     *
     * @return
     */
    private DataListener<UserToRoomDto> leaveUserFromRoom() {
        return (client, data, ackSender) -> {
            String securityToken = TextUtils.isEmpty(client.get(SECURITY_TOKEN_FIELD)) ? data.getSecurityToken() : client.get(SECURITY_TOKEN_FIELD);
            UserDto userDto = roomService.leaveUserFromRoom(data, securityToken);
            client.leaveRoom(String.valueOf(data.getRoomId()));
            log.info("User: " + data.getUserId() + " - left from room: " + data.getRoomId());
            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                    .sendEvent(LEFT_USER_EVENT, userDto);
            try {
                RoomDto roomDto = roomService.getRoom(data.getRoomId(), securityToken);
                if (roomDto.getGameStatus().equals(GUESSING_WORDS.getCode())) {
                    List<PlayerDto> playerDtoList = playerService.getPlayersInRoom(data.getRoomId(), securityToken);
                    QuestionDto lastQuestionDto = roomService.getLastQuestion(data.getRoomId(), securityToken);
                    PlayerDto playerDto = playerService.getPlayer(data.getRoomId(), data.getUserId(), securityToken);
                    if (playerDto.getAttempt() != 0 && !playerDto.isWinner()) {
                        PlayerGuessingDto nextGuessingPlayer = gameService.getNextGuessingPlayer(data.getRoomId(), lastQuestionDto, securityToken);
                        if (nextGuessingPlayer != null) {
                            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                    .sendEvent(SERVER_USER_GUESSING_EVENT, nextGuessingPlayer);
                            roomService.deleteEmptyQuestions(data.getRoomId(), data.getUserId(), securityToken);
                            return;
                        }
                    }
                    int playersNumber = getActivePlayersNumber(playerDtoList) - 1;
                    playersNumber = playersNumber > 0 ? playersNumber : 1;
                    if (lastQuestionDto.getWinVoters().size() / (playersNumber) >= PASS_POLL_PERCENT_VALUE) {
                        PlayerDto winPlayer = playerService.setPlayerWin(lastQuestionDto, securityToken);
                        socketIONamespace.getRoomOperations(String.valueOf((Long) client.get(ROOM_ID_FIELD)))
                                .sendEvent(SERVER_USER_WIN_EVENT, winPlayer);
                        GameOverDto gameOverDto = gameService.isGameOver(data.getRoomId(), securityToken);
                        if (gameOverDto.isGameOver()) {
                            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                    .sendEvent(SERVER_GAME_OVER, new UserToRoomDto());
                        } else {
                            PlayerGuessingDto nextGuessingPlayerDto = gameService.getNextGuessingPlayer(data.getRoomId(), lastQuestionDto, securityToken);
                            if (nextGuessingPlayerDto.getPlayer().getStatus() == AFK.getCode())
                                startPlayerAfkTimer(client);
                            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                    .sendEvent(SERVER_USER_GUESSING_EVENT, nextGuessingPlayerDto);
                        }
                    } else {
                        double yesNoVotersPercentage = (double) (lastQuestionDto.getNoVoters().size() + lastQuestionDto.getYesVoters().size())
                                / (playersNumber);
                        double allVotersPercentage = (double) (lastQuestionDto.getNoVoters().size() + lastQuestionDto.getYesVoters().size()
                                + lastQuestionDto.getWinVoters().size()) / (playersNumber);
                        if (yesNoVotersPercentage >= PASS_POLL_PERCENT_VALUE || allVotersPercentage == 1) {
                            PlayerGuessingDto nextGuessingPlayerDto = gameService.getNextGuessingPlayer(data.getRoomId(), lastQuestionDto, securityToken);
                            if (nextGuessingPlayerDto.getPlayer().getStatus() == AFK.getCode())
                                startPlayerAfkTimer(client);
                            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                    .sendEvent(SERVER_USER_GUESSING_EVENT, nextGuessingPlayerDto);
                        }
                    }
                    roomService.deleteEmptyQuestions(data.getRoomId(), data.getUserId(), securityToken);
                } else if (roomDto.getGameStatus().equals(SETTING_WORDS.getCode())) {
                    socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId())).sendEvent(SERVER_UPDATE_USERS_INFO, roomDto);
                    tryToStartGuessing(client, data.getRoomId());
                }
            } catch (ApiException e) {
                if (e.getCode() != NotFoundError.NO_SUCH_ROOM.getErrorCode()) {
                    throw e;
                }
            }
        };
    }

    private void tryToStartGuessing(SocketIOClient client, Long roomId) {
        try {
            List<PlayerDto> playersDtos = playerService.getPlayersInRoom(roomId, client.get(SECURITY_TOKEN_FIELD));
            boolean gameIsReady = true;
            for (PlayerDto player : playersDtos) {
                if (TextUtils.isEmpty(player.getWord()) && player.getStatus() != PlayerStatus.LEFT.getCode())
                    gameIsReady = false;
            }
            if (gameIsReady) {
                PlayerGuessingDto playerGuessingDto = gameService.startGuessingAndGettingGuessingPlayer(roomId, client.get(SECURITY_TOKEN_FIELD));
                if (playerGuessingDto != null) {
                    socketIONamespace.getRoomOperations(String.valueOf(roomId)).sendEvent(SERVER_USER_GUESSING_EVENT, playerGuessingDto);
                    log.info("Players start guessing: " + playerGuessingDto.getPlayer().getId());
                }
            }
        } catch (ApiException e) {
            if (e.getCode() != RoomError.GAME_ALREADY_STARTED.getErrorCode())
                throw e;
        }
    }


    /**
     * This method joining user to room and checking is room full if true then start game
     *
     * @return
     */
    private DataListener<UserToRoomDto> joinUserToRoom() {
        return (client, data, ackSender) -> {
            log.info("User tries to join to room");
            client.set(USER_ID_FIELD, data.getUserId());
            client.set(ROOM_ID_FIELD, data.getRoomId());
            client.set(SECURITY_TOKEN_FIELD, data.getSecurityToken());
            RoomDto roomDto = roomService.joinUserToRoom(data, data.getSecurityToken());
            client.sendEvent(ROOM_INFO_EVENT, roomDto);
            client.joinRoom(String.valueOf(roomDto.getId()));

            PlayerDto playerDto = null;
            for (PlayerDto player : roomDto.getPlayers()) {
                if (player.getUserId().equals(data.getUserId()))
                    playerDto = player;
            }

            socketIONamespace.getRoomOperations(String.valueOf(roomDto.getId())).sendEvent(JOINED_USER_EVENT, playerDto);

            int clientsNumber = socketIONamespace.getRoomOperations(String.valueOf(roomDto.getId())).getClients().size();
            log.info("User " + data.getUserId() + " - joined to room: " + data.getRoomId());
            log.info("Players in the room " + roomDto.getId() + " - " + roomDto.getCurrentPlayersNumber());
            log.info("Clients in the room " + roomDto.getId() + " - " + clientsNumber);

            //Start game
            if ((roomDto.getGameStatus().equals(WAITING_FOR_PLAYERS.getCode())) &&
                    (roomDto.getMaxPlayers().equals(roomDto.getPlayers().size())))
                startGame(roomDto, client.get(SECURITY_TOKEN_FIELD));
        };
    }

    @Async
    public void startGame(RoomDto room, String securityToken) {
        try {
            Thread.sleep(1000);
            RoomDto roomDto = gameService.startGame(room.getId(), securityToken);
            Map<SocketIOClient, PlayerDto> playersNeedToSendEvent = new HashMap<>();
            for (SocketIOClient client : socketIONamespace.getRoomOperations(String.valueOf(room.getId())).getClients()) {
                for (PlayerDto player : roomDto.getPlayers()) {
                    if (player.getUserId().equals(client.get(USER_ID_FIELD))) {
                        if (client.isChannelOpen()) {
                            playersNeedToSendEvent.put(client, player);
                        }
                    }
                }
            }
            for (PlayerDto playerDto : room.getPlayers()) {
                if (playerDto.getStatus() == AFK.getCode()) {
                    gameService.startAfkCountdown(
                            new UserToRoomDto.Builder()
                                    .setUserId(playerDto.getUserId())
                                    .setRoomId(roomDto.getId())
                                    .setSecurityToken(securityToken)
                                    .build(),
                            socketIONamespace.getRoomOperations(String.valueOf(roomDto.getId()))
                    );
                }
            }
            sendSetWordEvents(playersNeedToSendEvent, roomDto, securityToken);
            log.info("Game started in room: " + room.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startPlayerAfkTimer(SocketIOClient client) {
        gameService.startAfkCountdown(
                new UserToRoomDto.Builder()
                        .setUserId((Long) client.get(USER_ID_FIELD))
                        .setRoomId((Long) client.get(ROOM_ID_FIELD))
                        .setSecurityToken(client.get(SECURITY_TOKEN_FIELD))
                        .build(),
                socketIONamespace.getRoomOperations(String.valueOf((Long) client.get(ROOM_ID_FIELD)))
        );
    }

    private void sendSetWordEvents(Map<SocketIOClient, PlayerDto> playersNeedToSendEvent, RoomDto room, String securityToken) {
        List<SetWordDto> setWordDtos = gameService.getSetWordDtos(room.getId(), playersNeedToSendEvent.values(), securityToken);
        for (Map.Entry<SocketIOClient, PlayerDto> entry : playersNeedToSendEvent.entrySet()) {
            for (SetWordDto setWordDto : setWordDtos) {
                if (entry.getValue().getUserId().equals(setWordDto.getSenderUser().getId()))
                    entry.getKey().sendEvent(SERVER_SET_PLAYER_WORD_EVENT, setWordDto);
            }
        }
    }

    /**
     * Method send to all users in the room that some user started type
     *
     * @return
     */
    private DataListener<UserToRoomDto> userStartTyping() {
        return (client, data, ackSender) -> {
            data.setSecurityToken(client.get(SECURITY_TOKEN_FIELD));
            userTyping(data, SERVER_TYPING_EVENT);
        };
    }

    /**
     * Method send to all users in the room that some user stopped type
     *
     * @return
     */
    private DataListener<UserToRoomDto> userStopTyping() {
        return (client, data, ackSender) -> {
            data.setSecurityToken(client.get(SECURITY_TOKEN_FIELD));
            userTyping(data, SERVER_STOP_TYPING_EVENT);
            log.info("User stopped typing");
        };
    }

    /**
     * Inner method that sends events about user typing
     *
     * @param data
     * @param event
     */
    private void userTyping(UserToRoomDto data, String event) {
        PlayerDto playerDto = playerService.reconnect(data, data.getSecurityToken());
        if (playerDto == null) {
            throw ExceptionFactory.create(NotFoundError.NO_SUCH_USER_IN_ROOM);
        }
        HashMap<String, Long> userId = new HashMap<>();
        userId.put("userId", playerDto.getUserId());
        socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId())).sendEvent(event, userId);
    }

    /**
     * Method send out to all users in room message that user sent
     *
     * @return
     */
    private DataListener<MessageDto> sendUserMessage() {
        return (client, data, ackSender) -> {
            data.setType(MessageType.SIMPLE_MESSAGE.getCode());
            data.setSecurityToken(null);
            MessageDto resultMessage = roomService.sendMessage(data, client.get(SECURITY_TOKEN_FIELD));
            resultMessage.setClientMessageId(data.getClientMessageId());
            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId())).sendEvent(SERVER_MESSAGE_EVENT, resultMessage);
            log.info("User sent message: ", data);
        };
    }

    private DataListener<WordDto> setWordToPlayer() {
        return (client, data, ackSender) -> {
            data.setSecurityToken(null);
            SetWordDto setWordDto = gameService.setPlayerWord(data, client.get(SECURITY_TOKEN_FIELD));
            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId())).sendEvent(SERVER_THOUGHT_PLAYER_WORD_EVENT, setWordDto);
            log.info("User set word: " + data.getWord());

            tryToStartGuessing(client, data.getRoomId());
        };
    }

    private DataListener<QuestionDto> addUserQuestion() {
        return (client, data, ackSender) -> {
            log.info("Client: " + client.getHandshakeData().getAddress() + "\nUser: " + client.get(USER_ID_FIELD) + " asking question: " + data.getText());
            QuestionDto lastQuestion = roomService.getLastQuestion(data.getRoomId(), client.get(SECURITY_TOKEN_FIELD));
            if (lastQuestion.getQuestionId().equals(data.getQuestionId())) {
                QuestionDto resultMessage = gameService.addQuestion(data, client.get(SECURITY_TOKEN_FIELD));
                resultMessage.setClientMessageId(data.getClientMessageId());
                socketIONamespace.getRoomOperations(String.valueOf((Long) client.get(ROOM_ID_FIELD)))
                        .sendEvent(SERVER_USER_ASKING_EVENT, resultMessage);
            }
        };
    }

    private DataListener<QuestionDto> addVoteToQuestion() {
        return (client, data, ackSender) -> {
            data.setSecurityToken(null);
            log.info("add vote input: " + data.toString());

            QuestionDto questionDto = gameService.addVote(data, client.get(SECURITY_TOKEN_FIELD));
            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId())).sendEvent(SERVER_USER_ANSWERING_EVENT, questionDto);
            log.info(questionDto.toString());

            List<PlayerDto> playerDtoList = playerService.getPlayersInRoom(data.getRoomId(), client.get(SECURITY_TOKEN_FIELD));
            QuestionDto lastQuestionDto = roomService.getLastQuestion(data.getRoomId(), client.get(SECURITY_TOKEN_FIELD));
            int playersNumber = getActivePlayersNumber(playerDtoList) - 1;
            playersNumber = playersNumber > 0 ? playersNumber : 1;
            try {
                if (questionDto.getWinVoters().size() / (playersNumber) >= PASS_POLL_PERCENT_VALUE) {
                    PlayerDto winPlayer = playerService.setPlayerWin(questionDto, client.get(SECURITY_TOKEN_FIELD));
                    socketIONamespace.getRoomOperations(String.valueOf((Long) client.get(ROOM_ID_FIELD)))
                            .sendEvent(SERVER_USER_WIN_EVENT, winPlayer);
                    GameOverDto gameOverDto = gameService.isGameOver(data.getRoomId(), client.get(SECURITY_TOKEN_FIELD));
                    if (gameOverDto.isGameOver()) {
                        socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                .sendEvent(SERVER_GAME_OVER, new UserToRoomDto());
                    } else {
                        PlayerDto questioner = roomService.getQuestioner(
                                data.getRoomId(), questionDto.getQuestionId(), client.get(SECURITY_TOKEN_FIELD)
                        );
                        PlayerDto lastQuestioner = roomService.getQuestioner(
                                data.getRoomId(), lastQuestionDto.getQuestionId(), client.get(SECURITY_TOKEN_FIELD)
                        );
                        if (questioner.getId().equals(lastQuestioner.getId())) {
                            PlayerGuessingDto nextGuessingPlayerDto = gameService.getNextGuessingPlayer(
                                    data.getRoomId(), lastQuestionDto, client.get(SECURITY_TOKEN_FIELD)
                            );
                            if (nextGuessingPlayerDto.getPlayer().getStatus() == AFK.getCode())
                                startPlayerAfkTimer(client);
                            socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                    .sendEvent(SERVER_USER_GUESSING_EVENT, nextGuessingPlayerDto);
                        }
                    }
                } else if (lastQuestionDto.getQuestionId().equals(questionDto.getQuestionId())) {
                    double yesNoVotersPercentage = (double) (
                            questionDto.getNoVoters().size() + questionDto.getYesVoters().size()
                    ) / (playersNumber);
                    double allVotersPercentage = (double) (
                            questionDto.getNoVoters().size() + questionDto.getYesVoters().size() + questionDto.getWinVoters().size()
                    ) / (playersNumber);
                    if (yesNoVotersPercentage >= PASS_POLL_PERCENT_VALUE || allVotersPercentage == 1) {
                        PlayerGuessingDto nextGuessingPlayerDto = gameService.getNextGuessingPlayer(
                                data.getRoomId(), questionDto, client.get(SECURITY_TOKEN_FIELD)
                        );
                        if (nextGuessingPlayerDto.getPlayer().getStatus() == AFK.getCode())
                            startPlayerAfkTimer(client);
                        socketIONamespace.getRoomOperations(String.valueOf(data.getRoomId()))
                                .sendEvent(SERVER_USER_GUESSING_EVENT, nextGuessingPlayerDto);
                    }
                }
            } catch (ApiException e) {
                if (e.getCode() != RoomError.ALREADY_SENT_NEXT_PLAYER.getErrorCode())
                    throw e;
            }
        };
    }


    private Integer getActivePlayersNumber(List<PlayerDto> players) {
        int activePlayersNumber = 0;
        for (PlayerDto player : players) {
            if (player.getStatus() != PlayerStatus.LEFT.getCode())
                activePlayersNumber++;
        }
        return activePlayersNumber;
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client disconnected userId: " + client.get(USER_ID_FIELD));
            if (client.get(USER_ID_FIELD) != null) {
                UserToRoomDto userToRoom = new UserToRoomDto.Builder()
                        .setUserId(client.get(USER_ID_FIELD))
                        .setRoomId(client.get(ROOM_ID_FIELD))
                        .build();
                try {
                    PlayerDto playerDto = playerService.disconnect(userToRoom, client.get(SECURITY_TOKEN_FIELD));
                    if (playerDto.getStatus() == AFK.getCode()) {
                        socketIONamespace.getRoomOperations(String.valueOf((Long) client.get(ROOM_ID_FIELD)))
                                .sendEvent(SERVER_USER_AFK_EVENT, playerDto);
                        log.info("Sent afk event!");
                        RoomDto roomDto = roomService.getRoom(client.get(ROOM_ID_FIELD), client.get(SECURITY_TOKEN_FIELD));
                        PlayerDto wordReceiver = playerService.getPlayer(userToRoom.getRoomId(), playerDto.getWordSettingUserId(), client.get(SECURITY_TOKEN_FIELD));
                        if (roomDto.getGameStatus() == GameStatus.SETTING_WORDS.getCode() && TextUtils.isEmpty(wordReceiver.getWord())) {
                            startPlayerAfkTimer(client);
                        }
                    }
                } catch (ApiException e) {
                    if (e.getCode() != NotFoundError.NO_SUCH_USER_IN_ROOM.getErrorCode()) {
                        throw e;
                    }
                }
            }
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            log.info("Client connected! " + client.getHandshakeData().getAddress());
        };
    }
}
