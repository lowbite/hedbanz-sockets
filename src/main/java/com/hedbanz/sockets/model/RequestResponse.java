package com.hedbanz.sockets.model;

import com.hedbanz.sockets.error.CustomError;

public class RequestResponse<T> {
    private String status;
    private CustomError error;
    private T data;

    public RequestResponse(String status, CustomError error, T data){
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public CustomError getError() {
        return error;
    }

    public T getData() {
        return data;
    }
}
