package com.hedbanz.sockets.timer;

import com.corundumstudio.socketio.SocketIOServer;
import com.hedbanz.sockets.service.AdvertiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hedbanz.sockets.constant.SocketEvents.SERVER_ADVERTISE;

public class AdvertiseEventSender implements Runnable{
    private final SocketIOServer socketIOServer;
    private final AdvertiseService advertiseService;
    private final Logger log = LoggerFactory.getLogger(AdvertiseEventSender.class);

    public AdvertiseEventSender(SocketIOServer socketIOServer, AdvertiseService advertiseService) {
        this.socketIOServer = socketIOServer;
        this.advertiseService = advertiseService;
    }


    @Override
    public void run() {
        Integer type = advertiseService.getAdvertiseType();
        socketIOServer.getNamespace("/game").getBroadcastOperations().sendEvent(SERVER_ADVERTISE, type);
        log.info("Sent advertise event type of " + type);
    }
}
