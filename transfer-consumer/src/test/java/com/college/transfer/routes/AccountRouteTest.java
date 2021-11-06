package com.college.transfer.routes;

import com.college.transfer.entities.AccountEntity;
import com.college.transfer.repositories.AccountRepository;
import com.college.transfer.stub.AccountEntityStub;
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
import static org.mockito.BDDMockito.given;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class AccountRouteTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private CamelContext camelContext;
    @MockBean
    private AccountRepository accountRepository;

    private AccountEntityStub accountEntityStub;

    @Before
    public void setup() {
        this.accountEntityStub = new AccountEntityStub();
    }

    @Test
    public void shouldGetAccount() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.setProperty("accountNumber", "001");
        given(accountRepository.findByNumber("001"))
                .willReturn(accountEntityStub.buildAccountEntity(1, 1, "001", BigDecimal.TEN));

        Exchange result = producerTemplate.send("direct:getAccount", exchange);

        AccountEntity accountEntity = result.getProperty("accountEntity", AccountEntity.class);
        assertEquals(1, accountEntity.getCustomerId().intValue());
        assertEquals("001", accountEntity.getNumber());
        assertEquals(new BigDecimal("10"), accountEntity.getBalance());
    }
}
