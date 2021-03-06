package com.hedbanz.sockets.exception;

import com.hedbanz.sockets.error.MessageError;

import java.text.MessageFormat;

public class MessageException extends RuntimeException{
    private final MessageError error;

    public MessageException(MessageError error, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments));
        this.error = error;
    }

    public MessageException(MessageError error, final Throwable cause, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments), cause);
        this.error = error;
    }

    public int getCode() {
        return error.getErrorCode();
    }

    public String getMessage(){
        return error.getErrorMessage();
    }

    public MessageError getError(){
        return error;
    }
}
