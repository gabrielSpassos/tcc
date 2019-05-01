package com.college.transfer.stub;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;

public class MessageStub {

    public Message buildMessage() {
        return MessageBuilder
                .withBody("test".getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
    }
}
