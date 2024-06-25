package com.integra;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.client.RedisClientName;
import io.vertx.redis.client.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.Optional;

@ApplicationScoped
public class RedisService {

    @Inject
    RedisClient redisClient;

    public void set(String key, String value) {
        redisClient.set(Arrays.asList(key, value));
    }

    public Optional<String> get(String key) {
        Response response = redisClient.get(key);
        return response != null ? Optional.of(response.toString()) : Optional.empty();
    }
}