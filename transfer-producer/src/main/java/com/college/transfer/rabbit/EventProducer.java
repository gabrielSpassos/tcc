package com.college.transfer.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public EventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String exchange, String key, Message message) {
        logger.info("Enviando mensagem {}, para exchange {} com chave {}", message, exchange, key);
        rabbitTemplate.send(exchange, key, message);
    }
}
