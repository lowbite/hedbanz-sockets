package com.hedbanz.sockets.service.Implementation;

import com.hedbanz.sockets.error.InputError;
import com.hedbanz.sockets.exception.ExceptionFactory;
import com.hedbanz.sockets.service.RoomService;
import com.hedbanz.sockets.transfer.*;
import com.hedbanz.sockets.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hedbanz.sockets.constant.RequestsURI.*;

@Service
public class RoomServiceImpl implements RoomService {
    private final RequestHandler requestHandler;

    @Autowired
    public RoomServiceImpl(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public RoomDto getRoom(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return requestHandler.sendGetAndGetResultData(
                String.format(GET_ROOM_URI, roomId), securityToken, RoomDto.class
        ).orElseThrow(NullPointerException::new);
    }

    @Override
    public RoomDto joinUserToRoom(UserToRoomDto userToRoomDto, String securityToken) {
        if (userToRoomDto.getUserId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (userToRoomDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPostAndGetResultData(
                JOIN_USER_TO_ROOM_URI, userToRoomDto, securityToken, RoomDto.class
        ).orElseThrow(NullPointerException::new);
    }

    @Override
    public UserDto leaveUserFromRoom(UserToRoomDto userToRoomDto, String securityToken) {
        if (userToRoomDto.getUserId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (userToRoomDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPostAndGetResultData(
                LEAVE_USER_FROM_ROOM_URI, userToRoomDto, securityToken, UserDto.class
        ).orElseThrow(NullPointerException::new);
    }

    @Override
    public MessageDto sendMessage(MessageDto messageDto, String securityToken) {
        if (messageDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (messageDto.getText() == null)
            throw ExceptionFactory.create(InputError.EMPTY_MESSAGE_TEXT);
        if (messageDto.getSenderUser() == null || messageDto.getSenderUser().getId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_MESSAGE_SENDER);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPutAndGetResultData(
                ADD_USER_MESSAGE_URI, messageDto, securityToken, MessageDto.class
        ).orElseThrow(NullPointerException::new);
    }

    @Override
    public void sendWaitingForPlayersMessage(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        requestHandler.sendPut(
                String.format(ADD_WAITING_FOR_PLAYERS_MESSAGE, roomId), securityToken
        );
    }

    @Override
    public QuestionDto getLastQuestion(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return requestHandler.sendGetAndGetResultData(
                String.format(GET_LAST_QUESTION, roomId), securityToken, QuestionDto.class
        ).orElseThrow(NullPointerException::new);
    }

    @Override
    public PlayerDto getQuestioner(Long roomId, Long questionId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        if(questionId == null)
            throw ExceptionFactory.create(InputError.EMPTY_QUESTION_ID);
        return requestHandler.sendGetAndGetResultData(
                String.format(GET_QUESTIONER_URI, roomId, questionId), securityToken, PlayerDto.class
        ).orElseThrow(NullPointerException::new);
    }

    @Override
    public void deleteEmptyQuestions(Long roomId, Long userId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if(userId == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        requestHandler.sendDelete(String.format(DELETE_EMPTY_QUESTIONS, roomId, userId), securityToken);
    }
}
