package com.hedbanz.sockets.exception;

import com.hedbanz.sockets.error.ApiError;
import com.hedbanz.sockets.error.CustomError;
import com.hedbanz.sockets.error.FcmError;

import java.text.MessageFormat;

public class ApiException extends RuntimeException{
    private final ApiError error;

    public ApiException(ApiError error, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments));
        this.error = error;
    }

    public ApiException(ApiError error, final Throwable cause, Object... messageArguments) {
        super(MessageFormat.format(error.getErrorMessage(), messageArguments), cause);
        this.error = error;
    }

    public int getCode() {
        return error.getErrorCode();
    }

    public String getMessage(){
        return error.getErrorMessage();
    }

    public ApiError getError(){
        return error;
    }
}
