package com.hedbanz.sockets.timer;

import com.corundumstudio.socketio.BroadcastOperations;
import com.hedbanz.sockets.constant.NotificationMessageType;
import com.hedbanz.sockets.model.AfkWarning;
import com.hedbanz.sockets.model.FcmPush;
import com.hedbanz.sockets.model.Notification;
import com.hedbanz.sockets.service.GameService;
import com.hedbanz.sockets.service.RoomService;
import com.hedbanz.sockets.transfer.*;
import com.hedbanz.sockets.util.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

import static com.hedbanz.sockets.constant.GameStatus.SETTING_WORDS;
import static com.hedbanz.sockets.constant.PlayerStatus.AFK;
import static com.hedbanz.sockets.constant.RequestsURI.*;
import static com.hedbanz.sockets.constant.SocketEvents.*;

@Component
@Scope("prototype")
public class AfkTimerTask extends TimerTask {
    private final Logger log = LoggerFactory.getLogger("AFKTimerTask");
    //Time after which player will be kicked from room in ms
    private static final int ONE_MIN_IN_MS = 60000;

    private int timeLeft;
    private UserToRoomDto userToRoomDto;
    private Long period;
    private BroadcastOperations roomOperations;

    @Autowired
    private RequestHandler requestHandler;
    @Autowired
    private GameService gameService;
    @Autowired
    private RoomService roomService;

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setRoomOperations(BroadcastOperations roomOperations) {
        this.roomOperations = roomOperations;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public void setUserToRoomDto(UserToRoomDto userToRoomDto) {
        this.userToRoomDto = userToRoomDto;
    }

    @Override
    public void run() {
        PlayerDto player = requestHandler.sendGetAndGetResultData(
                String.format(GET_PLAYER_URI, userToRoomDto.getRoomId(), userToRoomDto.getUserId()), userToRoomDto.getSecurityToken(), PlayerDto.class
        );
        if (player == null) {
            log.info("No such player");
            cancel();
            return;
        }
        if (player.getStatus() == AFK.getCode()) {
            if (timeLeft == ONE_MIN_IN_MS) {
                UserDto userDto = requestHandler.sendGetAndGetResultData(
                        String.format(GET_USER_URI, userToRoomDto.getUserId()), userToRoomDto.getSecurityToken(), UserDto.class
                );
                roomOperations.sendEvent(SERVER_PLAYER_AFK_WARNING, userDto);
                log.info(SERVER_PLAYER_AFK_WARNING);
            } else if (timeLeft == ONE_MIN_IN_MS / 2) {
                RoomDto roomDto = roomService.getRoom(userToRoomDto.getRoomId(), userToRoomDto.getSecurityToken());
                if(roomDto == null)
                    cancel();
                AfkWarning warning = new AfkWarning(roomDto.getName(), roomDto.getId());
                requestHandler.sendPost(String.format(FCM_PUSH_AFK_WARNING_URI, userToRoomDto.getUserId()), warning, userToRoomDto.getSecurityToken());
                log.info("FCM Afk warning");
            } else if (timeLeft <= 0) {
                UserDto userDto = requestHandler.sendGetAndGetResultData(
                        String.format(GET_USER_URI, userToRoomDto.getUserId()), userToRoomDto.getSecurityToken(), UserDto.class
                );
                RoomDto roomDto = roomService.getRoom(userToRoomDto.getRoomId(), userToRoomDto.getSecurityToken());
                if(roomDto == null)
                    cancel();
                log.info(SERVER_KICKED_USER_EVENT);
                AfkWarning warning = new AfkWarning(roomDto.getName(), roomDto.getId());
                requestHandler.sendPost(String.format(FCM_PUSH_USER_KICKED_URI, userToRoomDto.getUserId()), warning, userToRoomDto.getSecurityToken());
                requestHandler.sendPost(LEAVE_USER_FROM_ROOM_URI, userToRoomDto, userToRoomDto.getSecurityToken());
                roomOperations.sendEvent(SERVER_KICKED_USER_EVENT, userDto);
                if( roomDto.getGameStatus().equals(SETTING_WORDS.getCode())) {
                    roomDto = roomService.getRoom(userToRoomDto.getRoomId(), userToRoomDto.getSecurityToken());
                    roomOperations.sendEvent(SERVER_UPDATE_USERS_INFO, roomDto);
                    PlayerGuessingDto playerGuessingDto = gameService.startGuessingAndGettingGuessingPlayer(userToRoomDto.getRoomId(), userToRoomDto.getSecurityToken());
                    if (playerGuessingDto != null) {
                        roomOperations.sendEvent(SERVER_USER_GUESSING_EVENT, playerGuessingDto);
                        log.info("Players start guessing: " + playerGuessingDto.getPlayer().getId());
                    }
                }
                cancel();
            }
            timeLeft -= period;
        } else {
            cancel();
            log.info("Player was returned!");
        }
    }
}
