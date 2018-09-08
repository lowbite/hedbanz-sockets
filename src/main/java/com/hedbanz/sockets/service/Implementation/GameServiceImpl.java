package com.hedbanz.sockets.service.Implementation;

import com.corundumstudio.socketio.BroadcastOperations;
import com.hedbanz.sockets.constant.RequestsURI;
import com.hedbanz.sockets.error.InputError;
import com.hedbanz.sockets.exception.ExceptionFactory;
import com.hedbanz.sockets.service.GameService;
import com.hedbanz.sockets.timer.AfkTimerTask;
import com.hedbanz.sockets.transfer.*;
import com.hedbanz.sockets.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;

import static com.hedbanz.sockets.constant.RequestsURI.*;

@Service
public class GameServiceImpl implements GameService{
    private final RequestHandler requestHandler;

    @Autowired
    public GameServiceImpl(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Lookup
    public AfkTimerTask getAfkTimerTask() {
        return null;
    }

    @Override
    public void startAfkCountdown(UserToRoomDto userToRoomDto, BroadcastOperations operations) {
        if (userToRoomDto.getUserId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (userToRoomDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);

        long period = 1000L;
        AfkTimerTask timerTask = getAfkTimerTask();
        timerTask.setUserToRoomDto(userToRoomDto);
        timerTask.setRoomOperations(operations);
        timerTask.setPeriod(period);
        timerTask.setTimeLeft(60000);
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, period);
    }

    @Override
    public SetWordDto setPlayerWord(WordDto wordDto, String securityToken) {
        if (wordDto.getSenderId() == null) {
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        }
        if (wordDto.getWord() == null){
            throw ExceptionFactory.create(InputError.EMPTY_WORD);
        }
        if(wordDto.getRoomId() == null) {
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        }
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPatchAndGetResultData(SET_WORD_URI, wordDto, securityToken, SetWordDto.class);
    }

    @Override
    public PlayerGuessingDto startGuessingAndGettingGuessingPlayer(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPostAndGetResultData(
                String.format(START_GUESSING_URI, roomId), securityToken, PlayerGuessingDto.class
        );
    }

    @Override
    public QuestionDto addQuestion(QuestionDto questionDto, String securityToken) {
        if (questionDto.getQuestionId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_QUESTION_ID);
        if (questionDto.getText() == null) {
            throw ExceptionFactory.create(InputError.EMPTY_MESSAGE_TEXT);
        }
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPostAndGetResultData(
                ADD_QUESTION_URI, questionDto, securityToken, QuestionDto.class
        );
    }

    @Override
    public QuestionDto addVote(QuestionDto questionDto, String securityToken) {
        if (questionDto.getQuestionId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_QUESTION_ID);
        if (questionDto.getSenderUser().getId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (questionDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (questionDto.getVote() == null)
            throw ExceptionFactory.create(InputError.EMPTY_VOTE_TYPE);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPostAndGetResultData(
                ADD_VOTE_URI, questionDto, securityToken, QuestionDto.class
        );
    }

    @Override
    public PlayerGuessingDto isQuestionerWin(QuestionDto questionDto, String securityToken) {
        if (questionDto.getQuestionId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_QUESTION_ID);
        if (questionDto.getRoomId() == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if(questionDto.getNoVoters() == null)
            throw ExceptionFactory.create(InputError.EMPTY_NO_VOTERS);
        if(questionDto.getYesVoters() == null)
            throw ExceptionFactory.create(InputError.EMPTY_YES_VOTERS);
        if(questionDto.getWinVoters() == null)
            throw ExceptionFactory.create(InputError.EMPTY_WIN_VOTERS);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendPostAndGetResultData(
                String.format(CHECK_QUESTIONER_WIN_URI, questionDto.getRoomId()), questionDto, securityToken, PlayerGuessingDto.class
        );
    }

    @Override
    public PlayerGuessingDto isGameOverElseGetNextGuessingPlayer(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return requestHandler.sendPostAndGetResultData(
                String.format(CHECK_GAME_OVER_URI,roomId), securityToken, PlayerGuessingDto.class
        );
    }

    @Override
    public RoomDto startGame(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return requestHandler.sendPostAndGetResultData(
                String.format(RequestsURI.START_GAME_URI, roomId), securityToken, RoomDto.class
        );
    }

    @Override
    public RoomDto restartGame(Long roomId, Long userId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if(userId == null)
            throw ExceptionFactory.create(InputError.EMPTY_USER_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);
        return requestHandler.sendPostAndGetResultData(
                String.format(RESTART_GAME_URI, roomId, userId), securityToken, RoomDto.class
        );
    }

    @Override
    public List<SetWordDto> getSetWordDtos(Long roomId, Collection<PlayerDto> playerDtoList, String securityToken) {
        if(playerDtoList == null)
            throw ExceptionFactory.create(InputError.EMPTY_PLAYERS_LIST);
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        SetWordDto[] setWordDtos = requestHandler.sendPostAndGetResultData(
                String.format(GET_SET_WORD_DTOS_URI, roomId), playerDtoList,
                securityToken, SetWordDto[].class
        );
        return Arrays.asList(setWordDtos);
    }

    @Override
    public PlayerGuessingDto getNextGuessingPlayer(Long roomId, String securityToken) {
        if(roomId == null)
            throw ExceptionFactory.create(InputError.EMPTY_ROOM_ID);
        if (securityToken == null)
            throw ExceptionFactory.create(InputError.EMPTY_SECURITY_TOKEN);

        return requestHandler.sendGetAndGetResultData(
                String.format(GET_NEXT_GUESSING_PLAYER, roomId), securityToken, PlayerGuessingDto.class
        );
    }
}
