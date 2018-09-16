package com.hedbanz.sockets.service;

import com.hedbanz.sockets.transfer.*;

public interface RoomService {
    RoomDto getRoom(Long roomId, String securityToken);

    RoomDto joinUserToRoom(UserToRoomDto userToRoomDto, String securityToken);

    UserDto leaveUserFromRoom(UserToRoomDto userToRoomDto, String securityToken);

    MessageDto sendMessage(MessageDto messageDto, String securityToken);

    void sendWaitingForPlayersMessage(Long roomId, String securityToken);

    QuestionDto getLastQuestion(Long roomId, String securityToken);

    PlayerDto getQuestioner(Long roomId, Long questionId, String securityToken);

    void deleteEmptyQuestions(Long roomId, Long userId, String securityToken);
}
