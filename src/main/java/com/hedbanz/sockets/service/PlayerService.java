package com.hedbanz.sockets.service;

import com.hedbanz.sockets.transfer.PlayerDto;
import com.hedbanz.sockets.transfer.UserToRoomDto;

import java.util.List;

public interface PlayerService {
    PlayerDto reconnect(UserToRoomDto userToRoomDto, String securityToken);

    PlayerDto disconnect(UserToRoomDto userToRoomDto, String securityToken);

    List<PlayerDto> getPlayersInRoom(Long roomId, String securityToken);

    PlayerDto getPlayer(Long roomId, Long userId, String securityToken);
}
