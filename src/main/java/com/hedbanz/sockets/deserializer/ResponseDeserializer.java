package com.hedbanz.sockets.deserializer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hedbanz.sockets.model.RequestResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ResponseDeserializer {
    private final Gson gson;

    public ResponseDeserializer(Gson gson) {
        this.gson = gson;
    }

    public <T> RequestResponse<T> deserialize(String json, Class<T> type){
        return gson.fromJson(json, getType(RequestResponse.class, type));
    }

    private Type getType(Class<?> rawType, Class<?> parameter){
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{parameter};
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
