package com.college.transfer.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static String EXCHANGE;
    public static String ROUTING_KEY;

    @Value("${rabbitmq.transfers.exchange}")
    public void setExchange(String exchange) {
        RabbitConfig.EXCHANGE = exchange;
    }

    @Value("${rabbitmq.transfers.routing-key}")
    public void setRoutingKey(String routingKey) {
        ROUTING_KEY = routingKey;
    }
}
