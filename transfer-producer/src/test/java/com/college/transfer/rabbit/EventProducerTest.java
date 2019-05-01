package com.college.transfer.rabbit;

import com.college.transfer.configs.RabbitConfig;
import com.college.transfer.stub.MessageStub;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EventProducerTest {

    private EventProducer eventProducer;
    private RabbitTemplate rabbitTemplate;
    private RabbitConfig rabbitConfig;
    private MessageStub messageStub;

    @Before
    public void setup() {
        this.rabbitTemplate = mock(RabbitTemplate.class);
        this.rabbitConfig = new RabbitConfig();
        this.rabbitConfig.setExchange("transfers-events");
        this.rabbitConfig.setRoutingKey("#");
        this.eventProducer = new EventProducer(rabbitTemplate);
        this.messageStub = new MessageStub();
    }

    @Test
    public void shouldSendMessageToRabbit() {
        Message message = messageStub.buildMessage();
        doNothing().when(rabbitTemplate).send(anyString(), anyString(), any());

        eventProducer.sendMessage(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);

        verify(rabbitTemplate, times(1)).send(eq("transfers-events"), eq("#"), eq(message));
    }
}
