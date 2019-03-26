package com.college.transfer.routes;

import com.college.transfer.configs.RabbitConfig;
import com.college.transfer.controllers.dto.TransferDTO;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class TransferRoute extends RouteBuilder {

    private RabbitConfig rabbitConfig;

    @Autowired
    public TransferRoute(RabbitConfig rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    @Override
    public void configure() {
        from("direct:createTransfer")
                .routeId("createTransfer")
                .validate(this::isTransferValid)
                .log(LoggingLevel.INFO, "Sending event ${body} to rabbit exchange")
                .marshal()
                .json(JsonLibrary.Jackson)
                .to(buildRabbitUri());
    }

    private Boolean isTransferValid(Exchange exchange) {
        TransferDTO event = exchange.getIn().getBody(TransferDTO.class);
        return Optional.ofNullable(event).isPresent();
    }

    private String buildRabbitUri() {
        return UriComponentsBuilder
                .newInstance()
                .scheme("rabbitmq")
                .host(getRabbitHost())
                .queryParam("queue", rabbitConfig.getRabbitQueue())
                .queryParam("exchangeType", "direct")
                .queryParam("username", rabbitConfig.getRabbitUser())
                .queryParam("password", rabbitConfig.getRabbitPass())
                .queryParam("autoDelete", false)
                .queryParam("autoAck", false)
                .queryParam("deadLetterExchangeType", "fanout")
                .queryParam("deadLetterExchange", "events.dead")
                .queryParam("deadLetterQueue", "dead.events")
                .queryParam("deadLetterRoutingKey", "#")
                .build()
                .toString();
    }

    private String getRabbitHost() {
        return String.format("%s:%s/%s",
                rabbitConfig.getRabbitUrl(),
                rabbitConfig.getRabbitPort(),
                rabbitConfig.getRabbitExchange());
    }
}
