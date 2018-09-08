package com.hedbanz.sockets.exception;

import com.hedbanz.sockets.error.PasswordResetError;

import java.text.MessageFormat;

public class PasswordResetException extends RuntimeException{
    private final PasswordResetError error;

    public PasswordResetException(PasswordResetError error, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments));
        this.error = error;
    }

    public PasswordResetException(PasswordResetError error, final Throwable cause, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments), cause);
        this.error = error;
    }

    public int getCode() {
        return error.getErrorCode();
    }

    public String getMessage(){
        return error.getErrorMessage();
    }

    public PasswordResetError getError(){
        return error;
    }
}
