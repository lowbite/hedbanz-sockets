package com.hedbanz.sockets.service;

import com.corundumstudio.socketio.BroadcastOperations;
import com.hedbanz.sockets.transfer.*;

import java.util.Collection;
import java.util.List;

public interface GameService {
    void startAfkCountdown(UserToRoomDto userToRoomDto, BroadcastOperations operations);

    SetWordDto setPlayerWord(WordDto wordDto, String securityToken);

    PlayerGuessingDto startGuessingAndGettingGuessingPlayer(Long roomId, String securityToken);

    QuestionDto addQuestion(QuestionDto questionDto, String securityToken);

    QuestionDto addVote(QuestionDto questionDto, String securityToken);

    PlayerGuessingDto isQuestionerWin(QuestionDto questionDto, String securityToken);

    PlayerGuessingDto isGameOverElseGetNextGuessingPlayer(Long roomId, String securityToken);

    RoomDto startGame(Long roomId, String securityToken);

    RoomDto restartGame(Long roomId, Long userId, String securityToken);

    List<SetWordDto> getSetWordDtos(Long roomId, Collection<PlayerDto> playerDtoList, String securityToken);

    PlayerGuessingDto getNextGuessingPlayer(Long roomId, String securityToken);
}
