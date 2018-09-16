package com.hedbanz.sockets.exception;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.hedbanz.sockets.error.CustomError;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class SocketExceptionListener implements ExceptionListener {
    private final Logger log = LoggerFactory.getLogger("SocketExceptionListener");

    @Override
    public void onEventException(Exception e, List<Object> list, SocketIOClient socketIOClient) {
        if (e instanceof ApiException) {
            socketIOClient.sendEvent("server-error", new CustomError(((ApiException) e).getCode(), e.getMessage()));
            log.error("APIException: " + e.getMessage());
        } else if (e instanceof InputException) {
            socketIOClient.sendEvent("server-error", new CustomError(((InputException) e).getCode(), e.getMessage()));
            log.error("Input Exception: " + e.getMessage());
        } else if (e instanceof NullPointerException) {
            log.error("Null Exception: " + e.getMessage());
            log.error("Trace" + Arrays.toString(e.getStackTrace()));
        } else {
            socketIOClient.sendEvent("server-error", new CustomError(500, e.getMessage()));
            log.error("Unidentified exception: " + e.getMessage());
        }
    }


    @Override
    public void onDisconnectException(Exception e, SocketIOClient socketIOClient) {

    }

    @Override
    public void onConnectException(Exception e, SocketIOClient socketIOClient) {

    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        channelHandlerContext.fireChannelInactive();
        log.error(throwable.getMessage());
        return false;
    }
}
