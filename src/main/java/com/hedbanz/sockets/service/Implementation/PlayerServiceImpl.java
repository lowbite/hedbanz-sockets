package com.hedbanz.sockets.service.Implementation;

import com.hedbanz.sockets.error.InputError;
import com.hedbanz.sockets.exception.ExceptionFactory;
import com.hedbanz.sockets.service.PlayerService;
import com.hedbanz.sockets.transfer.PlayerDto;
import com.hedbanz.sockets.transfer.UserToRoomDto;
import com.hedbanz.sockets.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hedbanz.sockets.constant.RequestsURI.*;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final RequestHandler requestHandler;

    @Autowired
    public PlayerServiceImpl(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public PlayerDto reconnect(UserToRoomDto userToRoomDto, String securityToken) {
        if (userToRoomDto.getUserId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (userToRoomDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPatchAndGetResultData(
                String.format(PLAYER_RECONNECT_URI, userToRoomDto.getUserId(), userToRoomDto.getRoomId()),
                securityToken, PlayerDto.class
        );
    }

    @Override
    public PlayerDto disconnect(UserToRoomDto userToRoomDto, String securityToken) {
        if (userToRoomDto.getUserId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (userToRoomDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return requestHandler.sendPostAndGetResultData(
                String.format(PLAYER_DISCONNECT_URI, userToRoomDto.getUserId(), userToRoomDto.getRoomId()),
                securityToken, PlayerDto.class);
    }

    @Override
    public List<PlayerDto> getPlayersInRoom(Long roomId, String securityToken) {
        if (roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        PlayerDto[] playerDtos = requestHandler.sendGetAndGetResultData(
                String.format(GET_PLAYERS_URI, roomId), securityToken, PlayerDto[].class
        );
        return Arrays.asList(playerDtos);
    }

    @Override
    public PlayerDto getPlayer(Long roomId, Long userId, String securityToken) {
        if (roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if(userId == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return  requestHandler.sendGetAndGetResultData(
                String.format(GET_PLAYER_URI, roomId, userId), securityToken, PlayerDto.class
        );
    }
}
