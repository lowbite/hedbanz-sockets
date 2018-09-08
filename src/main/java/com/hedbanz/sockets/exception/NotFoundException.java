package com.hedbanz.sockets.exception;

import com.hedbanz.sockets.error.NotFoundError;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {
    private final NotFoundError error;

    public NotFoundException(NotFoundError error, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments));
        this.error = error;
    }

    public NotFoundException(NotFoundError error, final Throwable cause, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments), cause);
        this.error = error;
    }

    public int getCode() {
        return error.getErrorCode();
    }

    public String getMessage(){
        return error.getErrorMessage();
    }

    public NotFoundError getError(){
        return error;
    }
}
