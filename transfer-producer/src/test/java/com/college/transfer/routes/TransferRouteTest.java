package com.college.transfer.routes;

import com.college.transfer.configs.RabbitConfig;
import com.college.transfer.model.TransferModel;
import com.college.transfer.rabbit.EventProducer;
import com.college.transfer.stub.TransferModelStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class TransferRouteTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private CamelContext camelContext;
    @MockBean
    private EventProducer eventProducer;
    @Autowired
    private ObjectMapper objectMapper;

    private RabbitConfig rabbitConfig;
    private TransferModelStub transferModelStub;

    @Before
    public void setup() {
        this.rabbitConfig = new RabbitConfig();
        this.rabbitConfig.setExchange("transfers-events");
        this.rabbitConfig.setRoutingKey("#");
        this.transferModelStub = new TransferModelStub();
    }

    @Test
    public void shouldSendMessageToExchange() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.setProperty("s3FixedFileKey", "s3-test-folder/file");
        exchange.getIn().setBody(transferModelStub.buildTransferModel());
        doNothing().when(eventProducer).sendMessage(eq("transfers-events"), eq("#"), any());

        producerTemplate.send("direct:createTransfer", exchange);

        verify(eventProducer, times(1)).sendMessage(any(), any(), any());
    }
}