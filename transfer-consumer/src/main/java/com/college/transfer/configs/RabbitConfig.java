package com.college.transfer.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Configuration
public class RabbitConfig {

    public static String URL;
    public static String PORT;
    public static String EXCHANGE;
    public static String QUEUE;
    public static String USER;
    public static String PASS;

    @Value("${rabbit.url}")
    public void setUrl(String url) {
        RabbitConfig.URL = url;
    }

    @Value("${rabbit.port}")
    public void setPort(String port){
        RabbitConfig.PORT = port;
    }

    @Value("${rabbit.exchange}")
    public void setExchange(String exchange) {
        RabbitConfig.EXCHANGE = exchange;
    }

    @Value("${rabbit.queue}")
    public void setQueue(String queue) {
        RabbitConfig.QUEUE = queue;
    }

    @Value("${rabbit.user}")
    public void setUser(String user) {
        RabbitConfig.USER = user;
    }

    @Value("${rabbit.pass}")
    public void setPass(String pass) {
        RabbitConfig.PASS = pass;
    }
}
