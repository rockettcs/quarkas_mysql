package com.integra;

import io.quarkus.redis.client.RedisClientName;
import io.vertx.redis.client.impl.RedisClient;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RedisRoute extends RouteBuilder {

    @Inject
    RedisService redisService;
    @Inject
    MyService myService;
    @Inject
    DataRepository dataRepository;

    @Override
    public void configure() throws Exception {
        // Route to set a value in Redis and MySQL
        from("direct:set")
            .process(exchange -> {
                String key = exchange.getIn().getHeader("key", String.class);
                String value = exchange.getIn().getHeader("value", String.class);

                myService.performDatabaseOperations(key, value);
                redisService.set(key, value);
            })
            .log("Set value in Redis and MySQL");

        // Route to get a value from Redis or MySQL
        from("direct:get")
                .process(exchange -> {
                    String key = exchange.getIn().getHeader("key", String.class);
                    redisService.get(key);
                })
            .choice()
                .when(body().isNotNull())
                    .log("Got value from Redis: ${body}")
                .otherwise()
                    .process(exchange -> {
                        String key = exchange.getIn().getHeader("key", String.class);
                        DataEntity dataEntity = dataRepository.findByKey(key);
                        if (dataEntity != null) {
                            redisService.set(key, dataEntity.getValue());
                            exchange.getIn().setBody(dataEntity.value);
                        } else {
                            exchange.getIn().setBody(null);
                        }
                    })
                    .log("Got value from MySQL: ${body}");
    }
}