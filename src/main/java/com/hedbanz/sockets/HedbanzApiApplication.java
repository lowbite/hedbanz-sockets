package com.hedbanz.sockets;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.hedbanz.sockets.deserializer.ResponseDeserializer;
import com.hedbanz.sockets.exception.SocketExceptionListener;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@EnableAsync
@SpringBootApplication
public class HedbanzApiApplication {

    @Value("${socketIO.hostname}")
    private String socketIOHostname;

    @Value("${socketIO.port}")
    private Integer socketIOPort;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration configuration = new Configuration();
        configuration.setHostname(socketIOHostname);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        configuration.setPingInterval(1000);
        configuration.setPingTimeout(5000);
        configuration.setExceptionListener(new SocketExceptionListener());
        configuration.setSocketConfig(socketConfig);
        configuration.setWorkerThreads(100);

        configuration.setPort(socketIOPort);
        return new SocketIOServer(configuration);
    }

    @Bean
    @Qualifier("SimpleTemplate")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    @Qualifier("PatchTemplate")
    public RestTemplate patchRestTemplate(){
        HttpClient httpClient = HttpClientBuilder.create().build();
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setThreadNamePrefix("Advertise thread");
        return taskScheduler;
    }

    @Bean
    public ResponseDeserializer responseDeserializer(){
        return new ResponseDeserializer(new Gson());
    }

    public static void main(String[] args) {
        SpringApplication.run(HedbanzApiApplication.class, args);
    }
}
