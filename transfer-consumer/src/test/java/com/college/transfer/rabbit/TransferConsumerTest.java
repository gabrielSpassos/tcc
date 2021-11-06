package com.college.transfer.rabbit;

import com.college.transfer.model.TransferModel;
import com.college.transfer.stub.TransferModelStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TransferConsumerTest {

    private ObjectMapper objectMapper;
    private ProducerTemplate producerTemplate;
    private TransferConsumer transferConsumer;
    private TransferModelStub transferModelStub;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        this.producerTemplate = mock(ProducerTemplate.class);
        this.transferConsumer = new TransferConsumer(objectMapper, producerTemplate);
        this.transferModelStub = new TransferModelStub();
    }

    @Test
    public void shouldConsumeTransfer() {
        TransferModel transferModel = transferModelStub.buildTransferModel("12", "001", "002");
        doNothing().when(producerTemplate).sendBody("direct:transferAmount", transferModel);

        transferConsumer.consumeTransfers(buildMessage(Resources.TRANSFER));

        verify(producerTemplate, times(1)).sendBody(eq("direct:transferAmount"), any());
    }

    private Message buildMessage(String body) {
        return MessageBuilder
                .withBody(body.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
    }

    public class Resources {
        static final String TRANSFER = "{\n" +
                "  \"amount\": 12,\n" +
                "  \"destinyAccountNumber\": \"002\",\n" +
                "  \"originAccountNumber\": \"001\"\n" +
                "}";
    }
}
