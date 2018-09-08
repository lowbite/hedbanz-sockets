package com.hedbanz.sockets.exception;

import com.hedbanz.sockets.error.RoomError;

import java.text.MessageFormat;

public class RoomException extends RuntimeException {
    private final RoomError error;

    public RoomException(RoomError error, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments));
        this.error = error;
    }

    public RoomException(RoomError error, final Throwable cause, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments), cause);
        this.error = error;
    }

    public int getCode() {
        return error.getErrorCode();
    }

    public String getMessage(){
        return error.getErrorMessage();
    }

    public RoomError getError(){
        return error;
    }
}
