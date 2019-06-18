package com.college.transfer.rabbit;

import com.college.transfer.dto.TransferDTO;
import com.college.transfer.model.TransferModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    private final ProducerTemplate producerTemplate;

    @Autowired
    public TransferConsumer(ObjectMapper objectMapper, ProducerTemplate producerTemplate) {
        this.objectMapper = objectMapper;
        this.producerTemplate = producerTemplate;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbitmq.queue.transfers}"),
            exchange = @Exchange(value = "${rabbitmq.exchange.name}", type = "${rabbitmq.exchange.type}"),
            key = "${rabbitmq.exchange.key}"))
    public void consumeTransfers(Message message) {
        try {
            TransferDTO transferDTO = objectMapper.readValue(message.getBody(), TransferDTO.class);
            logger.info("Mensagem encontrada: {}", transferDTO);
            producerTemplate.sendBody(
                    "direct:transferAmount",
                    convertTransferDTOToModel(transferDTO)
            );
        } catch (Exception e) {
            logger.error("Erro consumindo mensagem de transferencia.", e);
        }
    }

    private TransferModel convertTransferDTOToModel(TransferDTO transferDTO) {
        return objectMapper.convertValue(transferDTO, TransferModel.class);
    }
}
