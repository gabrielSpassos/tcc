package com.college.transfer.routes;

import com.college.transfer.configs.RabbitConfig;
import com.college.transfer.model.TransferModel;
import com.college.transfer.rabbit.EventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransferRoute extends RouteBuilder {

    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public TransferRoute(EventProducer eventProducer, ObjectMapper objectMapper) {
        this.eventProducer = eventProducer;
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "[ERRO] Falha ao transferir valor")
                .end();

        from("direct:createTransfer")
                .routeId("createTransfer")
                .validate(this::isTransferValid)
                .process(this::buildMessage)
                .process(this::sendMessage)
                .end();
    }

    private Boolean isTransferValid(Exchange exchange) {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        return Optional.ofNullable(transferModel).isPresent();
    }

    private void buildMessage(Exchange exchange) throws JsonProcessingException {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        byte[] binaryData = objectMapper.writeValueAsBytes(transferModel);
        Message message = buildMessage(binaryData);
        exchange.setProperty("message", message);
    }

    private void sendMessage(Exchange exchange) {
        Message message = exchange.getProperty("message", Message.class);
        eventProducer.sendMessage(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }

    private Message buildMessage(byte[] binaryData) {
        return MessageBuilder
                .withBody(binaryData)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
    }
}
