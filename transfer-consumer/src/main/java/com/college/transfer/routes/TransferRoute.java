package com.college.transfer.routes;

import com.college.transfer.configs.RabbitConfig;
import com.college.transfer.dto.TransferDTO;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TransferRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(getTransferRabbitUri())
                .routeId("transfer-consumer")
                .unmarshal()
                .json(JsonLibrary.Jackson, TransferDTO.class)
                .log(LoggingLevel.INFO, "Body: ${body}.")
                .end();
    }

    private String getTransferRabbitUri() {
        return UriComponentsBuilder
                .newInstance()
                .scheme("rabbitmq")
                .host(getRabbitHost())
                .queryParam("queue", RabbitConfig.QUEUE)
                .queryParam("exchangeType", "topic")
                .queryParam("username", RabbitConfig.USER)
                .queryParam("password", RabbitConfig.PASS)
                .queryParam("autoDelete", false)
                .queryParam("autoAck", false)
                .queryParam("automaticRecoveryEnabled", true)
                .queryParam("deadLetterExchangeType", "fanout")
                .queryParam("deadLetterExchange", "transfers.dead")
                .queryParam("deadLetterQueue", "dead.transfers")
                .queryParam("deadLetterRoutingKey", "#")
                .build()
                .toString();
    }

    private String getRabbitHost() {
        return String.format("%s:%s/%s", RabbitConfig.URL, RabbitConfig.PORT, RabbitConfig.EXCHANGE);
    }
}
