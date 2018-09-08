package com.hedbanz.sockets.socket;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hedbanz.sockets.transfer.LoginAnswerDto;
import com.hedbanz.sockets.transfer.LoginDto;
import com.hedbanz.sockets.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.hedbanz.sockets.constant.RequestsURI.CHECK_LOGIN_URI;
import static com.hedbanz.sockets.constant.SocketEvents.CLIENT_CHECK_LOGIN;
import static com.hedbanz.sockets.constant.SocketEvents.SERVER_CHECK_LOGIN;

@Component
public class LoginAvailabilityEventHandler {
    private final SocketIONamespace socketIONamespace;
    private final RequestHandler requestHandler;

    @Autowired
    public LoginAvailabilityEventHandler(SocketIOServer server, RequestHandler requestHandler){
        this.socketIONamespace = server.addNamespace("/login");
        this.socketIONamespace.addConnectListener(onConnected());
        this.socketIONamespace.addDisconnectListener(onDisconnected());
        this.socketIONamespace.addEventListener(CLIENT_CHECK_LOGIN, LoginDto.class, onRecieved());
        this.requestHandler = requestHandler;
    }

    private DataListener<LoginDto> onRecieved() {
        return (client, data, ackSender) -> {
            LoginAnswerDto answerDto = requestHandler.sendPostAndGetResultData(CHECK_LOGIN_URI, data, LoginAnswerDto.class);
            client.sendEvent(SERVER_CHECK_LOGIN, answerDto);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {};
    }

    private ConnectListener onConnected() {
        return client -> {};
    }
}
