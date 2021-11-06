package com.college.transfer.routes;

import com.college.transfer.repositories.AccountRepository;
import com.college.transfer.stub.AccountEntityStub;
import com.college.transfer.stub.TransferModelStub;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class BalanceRouteTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private CamelContext camelContext;
    @MockBean
    private AccountRepository accountRepository;

    private AccountEntityStub accountEntityStub;
    private TransferModelStub transferModelStub;

    @Before
    public void setup() {
        this.accountEntityStub = new AccountEntityStub();
        this.transferModelStub = new TransferModelStub();
    }

    @Test
    public void shouldTransferAmount() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(transferModelStub.buildTransferModel("10", "001", "002"));
        exchange.setProperty("originAccount", accountEntityStub.buildAccountEntity(1, 1, "001", BigDecimal.TEN));
        exchange.setProperty("destinyAccount", accountEntityStub.buildAccountEntity(2, 2, "002", BigDecimal.TEN));

        given(accountRepository.save(accountEntityStub.buildAccountEntity(1, 1, "001", BigDecimal.ZERO)))
                .willReturn(accountEntityStub.buildAccountEntity(1, 1, "001", BigDecimal.ZERO));
        given(accountRepository.save(accountEntityStub.buildAccountEntity(2, 2, "002", new BigDecimal("20"))))
                .willReturn(accountEntityStub.buildAccountEntity(2, 2, "002", new BigDecimal("20")));

        producerTemplate.send("direct:transferAmountBetweenAccounts", exchange);

        verify(accountRepository, times(1))
                .save(accountEntityStub.buildAccountEntity(1, 1, "001", BigDecimal.ZERO));
        verify(accountRepository, times(1))
                .save(accountEntityStub.buildAccountEntity(2, 2, "002", new BigDecimal("20")));
    }
}
