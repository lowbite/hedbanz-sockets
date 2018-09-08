package com.hedbanz.sockets.service.Implementation;

import com.hedbanz.sockets.constant.RequestsURI;
import com.hedbanz.sockets.service.AdvertiseService;
import com.hedbanz.sockets.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {
    private final RequestHandler requestHandler;

    @Autowired
    public AdvertiseServiceImpl(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public Integer getAdvertiseRate() {
        return requestHandler.sendGetAndGetResultData(RequestsURI.GET_ADVERTISE_RATE, null, Integer.class);
    }

    @Override
    public Integer getAdvertiseType() {
        return requestHandler.sendGetAndGetResultData(RequestsURI.GET_ADVERTISE_TYPE, null, Integer.class);
    }
}
