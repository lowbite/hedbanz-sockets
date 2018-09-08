package com.hedbanz.sockets;

import com.corundumstudio.socketio.SocketIOServer;
import com.hedbanz.sockets.service.AdvertiseService;
import com.hedbanz.sockets.timer.AdvertiseEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerRunner implements CommandLineRunner {
    private final SocketIOServer server;
    private final TaskScheduler taskScheduler;
    private final Trigger trigger;
    private final AdvertiseService advertiseService;

    @Autowired
    public SocketIOServerRunner(SocketIOServer server, TaskScheduler taskScheduler,
                                @Qualifier("advertiseTrigger") Trigger trigger, AdvertiseService advertiseService){
        this.server = server;
        this.taskScheduler = taskScheduler;
        this.trigger = trigger;
        this.advertiseService = advertiseService;
    }

    @Override
    public void run(String... strings) throws Exception {
        server.start();
        taskScheduler.schedule(new AdvertiseEventSender(server, advertiseService), trigger);
    }
}
