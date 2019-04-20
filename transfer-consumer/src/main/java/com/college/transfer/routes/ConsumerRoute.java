package com.college.transfer.routes;

import com.college.transfer.configs.RabbitConfig;
import com.college.transfer.dto.TransferDTO;
import com.college.transfer.model.TransferModel;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ConsumerRoute extends RouteBuilder {

    private final ModelMapper modelMapper;

    @Autowired
    public ConsumerRoute(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void configure() {
        from(getTransferRabbitUri())
                .routeId("transfer-consumer")
                .unmarshal()
                .json(JsonLibrary.Jackson, TransferDTO.class)
                .log(LoggingLevel.INFO, "Body: ${body}.")
                .process(this::convertBody)
                .to("direct:transferAmount")
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

    private void convertBody(Exchange exchange) {
        TransferDTO transferDTO = exchange.getIn().getBody(TransferDTO.class);
        TransferModel transferModel = convertTransferDTOToModel(transferDTO);
        exchange.getIn().setBody(transferModel, TransferModel.class);
    }

    private String getRabbitHost() {
        return String.format("%s:%s/%s", RabbitConfig.URL, RabbitConfig.PORT, RabbitConfig.EXCHANGE);
    }

    private TransferModel convertTransferDTOToModel(TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, TransferModel.class);
    }
}
