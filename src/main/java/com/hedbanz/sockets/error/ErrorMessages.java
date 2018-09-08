package com.hedbanz.sockets.error;

public interface ErrorMessages {

     String EMPTY_LOGIN_MESSAGE = "Empty login!";
     String EMPTY_PASSWORD_MESSAGE = "Empty password!";
     String EMPTY_EMAIL_MESSAGE = "Empty email!";
     String EMPTY_KEY_WORD = "Empty key word!";
     String EMPTY_LOCALE = "Empty locale!";
     String EMPTY_STICKER_ID = "Empty sticker id!";
     String EMPTY_ICON_ID = "Empty icon id!";
     String EMPTY_ROOM_ID = "Empty room id";
     String EMPTY_USER_ID = "Empty user id";
     String EMPTY_ROOM_NAME = "Empty room name";
     String EMPTY_QUESTION_ID  ="Empty question id";
     String EMPTY_MESSAGE_TEXT = "Empty message text";
     String EMPTY_MESSAGE_TYPE = "Empty message type";
     String EMPTY_SENDER_USER = "Empty message sender";
     String EMPTY_VOTE_TYPE = "Empty vote type";
     String EMPTY_PLAYERS_STATUS = "Empty player status";
     String EMPTY_WORD = "Empty word";
     String EMPTY_VERSION_FIELD = "Empty application version";
     String EMPTY_SECURITY_TOKEN = "Empty security token";
     String EMPTY_NO_VOTERS = "Empty no voters in question";
     String EMPTY_YES_VOTERS = "Empty yes voters in question";
     String EMPTY_WIN_VOTERS = "Empty win voters in question";
     String EMPTY_PLAYERS_LIST = "Empty player list";

     String SUCH_LOGIN_ALREADY_USING_MESSAGE = "Such login already exist!";
     String SUCH_EMAIL_ALREADY_USING_MESSAGE = "Such email already using!";
     String SUCH_PLAYER_ALREADY_VOTED = "This player has already voted!";
     String SUCH_ROOM_ALREADY_EXIST = "Room with such name already exist!";
     String SUCH_PLAYER_ALREADY_IN_ROOM = "This player already in room";

     String NO_SUCH_USER_MESSAGE = "Such user did not found!";
     String NO_SUCH_ROOM = "There is no such room!";
     String NO_SUCH_USER_IN_ROOM = "There is no such user in room!";
     String NO_SUCH_QUESTION = "There is no such question!";
     String NO_SUCH_PLAYER = "There is no such player!";
     String NO_SUCH_MESSAGE = "No such message in this room!";

     String INCORRECT_ROOM_ID = "Incorrect room id!";
     String INCORRECT_USER_ID = "Incorrect user id!";
     String INCORRECT_PASSWORD = "Invalid password!";
     String INCORRECT_LOGIN = "Invalid login!";
     String INCORRECT_EMAIL = "Invalid email!";
     String INCORRECT_KEY_WORD = "Incorrect key word";
     String INCORRECT_CREDENTIALS = "Incorrect login or password";
     String INCORRECT_LOCALE = "Incorrect locale";
     String INCORRECT_VERSION_FIELD = "Incorrect application version";
     String INCORRECT_FILTER = "Incorrect filter";

     String ALREADY_FRIENDS = "You are already friends!";
     String NOT_FRIENDS = "You are not friends!";
     String CANT_SEND_FRIENDSHIP_REQUEST = "Can't send friendship request to this user at this time";

     String ROOM_IS_FULL = "Room is full!";
     String CANT_START_GAME = "Impossible to start game!";
     String GAME_HAS_BEEN_ALREADY_STARTED = "Game has been started!";
     String MAX_ACTIVE_ROOMS_NUMBER = "Player already plays maximal number of allowed games!";
     String KEY_WORD_IS_EXPIRED = "Key word is expired";

     String CANT_SEND_MESSAGE_NOTIFICATION = "Can't send message push notification!";
}