package com.hedbanz.sockets.util;

import com.hedbanz.sockets.constant.ResponseStatus;
import com.hedbanz.sockets.deserializer.ResponseDeserializer;
import com.hedbanz.sockets.error.ApiError;
import com.hedbanz.sockets.error.CustomError;
import com.hedbanz.sockets.exception.ExceptionFactory;
import com.hedbanz.sockets.model.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestHandler {
    private final static String BEARER_TOKEN = "Bearer %s";
    private final RestTemplate restTemplate;
    private final RestTemplate patchRestTemplate;
    private final ResponseDeserializer responseDeserializer;

    @Autowired
    public RequestHandler(@Qualifier("SimpleTemplate") RestTemplate restTemplate,
                          @Qualifier("PatchTemplate") RestTemplate patchRestTemplate,
                          ResponseDeserializer responseDeserializer) {
        this.restTemplate = restTemplate;
        this.patchRestTemplate = patchRestTemplate;
        this.responseDeserializer = responseDeserializer;
    }

    public <T> T sendPostAndGetResultData(String uri, Object dataToSend, String securityToken, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(dataToSend, headers);
        String responseJson = restTemplate.postForObject(uri, entity, String.class);
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseJson, type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public <T> T sendPostAndGetResultData(String uri, Object dataToSend, Class<T> type) {
        HttpEntity entity = new HttpEntity<>(dataToSend);
        String responseJson = restTemplate.postForObject(uri, entity, String.class);
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseJson, type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public <T> T sendPostAndGetResultData(String uri, String securityToken, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(null, headers);
        String responseJson = restTemplate.postForObject(uri, entity, String.class);
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseJson, type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public void sendPost(String uri, String securityToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(null, headers);
        String responseJson = restTemplate.postForObject(uri, entity, String.class);
        RequestResponse<?> requestResponse = responseDeserializer.deserialize(responseJson, Object.class);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
    }

    public void sendPost(String uri, Object dataToSend, String securityToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(dataToSend, headers);
        String responseJson = restTemplate.postForObject(uri, entity, String.class);
        RequestResponse<?> requestResponse = responseDeserializer.deserialize(responseJson, Object.class);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
    }

    public <T> T sendGetAndGetResultData(String uri, String securityToken, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(null, headers);
        String responseJson = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseJson, type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public <T> T sendPatchAndGetResultData(String uri, Object dataToSend, String securityToken, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(dataToSend, headers);
        String responseJson = patchRestTemplate.exchange(uri, HttpMethod.PATCH, entity, String.class).getBody();
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseJson, type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public <T> T sendPatchAndGetResultData(String uri, String securityToken, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(null, headers);
        String responseJson = patchRestTemplate.exchange(uri, HttpMethod.PATCH, entity, String.class).getBody();
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseJson, type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public <T> T sendPutAndGetResultData(String uri, Object dataToSend, String securityToken, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(dataToSend, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        RequestResponse<T> requestResponse = responseDeserializer.deserialize(responseEntity.getBody(), type);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
        else
            return requestResponse.getData();
    }

    public void sendPut(String uri, String securityToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
        RequestResponse<?> requestResponse = responseDeserializer.deserialize(responseEntity.getBody(), Object.class);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
    }

    public void sendDeleteAndGetResultData(String uri, Object dataToSend, String securityToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format(BEARER_TOKEN, securityToken));
        HttpEntity entity = new HttpEntity<>(dataToSend, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        RequestResponse<?> requestResponse = responseDeserializer.deserialize(responseEntity.getBody(), Object.class);
        if (requestResponse.getStatus().equals(ResponseStatus.ERROR_STATUS))
            throw ExceptionFactory.create(new ApiError(
                    requestResponse.getError().getErrorCode(), requestResponse.getError().getErrorMessage()));
    }
}
