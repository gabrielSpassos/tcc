package com.college.transfer.routes;

import com.college.transfer.entities.AccountEntity;
import com.college.transfer.factories.TransferEntityFactory;
import com.college.transfer.repositories.TransferRepository;
import com.college.transfer.stub.AccountEntityStub;
import com.college.transfer.stub.CustomerEntityStub;
import com.college.transfer.stub.TransferEntityStub;
import com.college.transfer.stub.TransferModelStub;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    private TransferRepository transferRepository;
    @Autowired
    private TransferEntityFactory transferEntityFactory;

    private TransferModelStub transferModelStub;
    private AccountEntityStub accountEntityStub;
    private CustomerEntityStub customerEntityStub;
    private TransferEntityStub transferEntityStub;

    private static final String DIRECT_GET_ACCOUNT = "direct:getAccount";
    private static final String MOCK_GET_ACCOUNT = "mock:getAccount";
    private static final String DIRECT_GET_CUSTOMERS = "direct:getCustomers";
    private static final String MOCK_GET_CUSTOMERS = "mock:getCustomers";
    private static final String DIRECT_TRANSFER_AMOUNT = "direct:transferAmountBetweenAccounts";
    private static final String MOCK_TRANSFER_AMOUNT = "mock:transferAmountBetweenAccounts";

    @Before
    public void setup() {
        this.transferModelStub = new TransferModelStub();
        this.accountEntityStub = new AccountEntityStub();
        this.customerEntityStub = new CustomerEntityStub();
        this.transferEntityStub = new TransferEntityStub();
    }

    @Test
    public void shouldTransferAmount() {
        setupCamelTransactionContext();
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(transferModelStub.buildTransferModel("100", "001", "002"));
        exchange.setProperty("accountEntity", new AccountEntity());
        exchange.setProperty("originAccount", accountEntityStub
                .buildAccountEntity(1, 1, "001", BigDecimal.TEN));
        exchange.setProperty("originCustomerEntity", customerEntityStub
                .buildCustomer(1, "Gabriel", "19775967031", LocalDate.parse("1997-04-21")));
        exchange.setProperty("destinyAccount", accountEntityStub
                .buildAccountEntity(2, 2, "002", BigDecimal.TEN));
        exchange.setProperty("destinyCustomerEntity", customerEntityStub
                .buildCustomer(2, "Thomas", "70265996074", LocalDate.parse("1995-06-18")));

        given(transferRepository.save(any()))
                .willReturn(transferEntityStub.buildTransferEntity(
                        1, 1, 1, 2, 2, BigDecimal.TEN));

        producerTemplate.send("direct:transferAmount", exchange);

        verify(transferRepository, times(1)).save(any());
    }

    private void setupCamelTransactionContext() {
        try {
            camelContext.getRouteDefinition("transferAmount")
                    .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                        @Override
                        public void configure() {
                            interceptSendToEndpoint(DIRECT_GET_ACCOUNT)
                                    .skipSendToOriginalEndpoint()
                                    .to(MOCK_GET_ACCOUNT);
                            interceptSendToEndpoint(DIRECT_GET_CUSTOMERS)
                                    .skipSendToOriginalEndpoint()
                                    .to(MOCK_GET_CUSTOMERS);
                            interceptSendToEndpoint(DIRECT_TRANSFER_AMOUNT)
                                    .skipSendToOriginalEndpoint()
                                    .to(MOCK_TRANSFER_AMOUNT);
                        }
                    });
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
