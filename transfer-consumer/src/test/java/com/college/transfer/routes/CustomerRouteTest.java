package com.college.transfer.routes;

import com.college.transfer.entities.CustomerEntity;
import com.college.transfer.repositories.CustomerRepository;
import com.college.transfer.stub.AccountEntityStub;
import com.college.transfer.stub.CustomerEntityStub;
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
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class CustomerRouteTest {

    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private CamelContext camelContext;
    @MockBean
    private CustomerRepository customerRepository;

    private AccountEntityStub accountEntityStub;
    private CustomerEntityStub customerEntityStub;

    @Before
    public void setup() {
        this.accountEntityStub = new AccountEntityStub();
        this.customerEntityStub = new CustomerEntityStub();
    }

    @Test
    public void shouldGetCustomers() {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.setProperty("originAccount", accountEntityStub.buildAccountEntity(1, 1, "001", BigDecimal.TEN));
        given(customerRepository.findById(1))
                .willReturn(customerEntityStub.buildCustomerEntity(1, "Gabriel", "19775967031", LocalDate.parse("1997-04-21")));

        exchange.setProperty("destinyAccount", accountEntityStub.buildAccountEntity(2, 2, "002", BigDecimal.TEN));
        given(customerRepository.findById(2))
                .willReturn(customerEntityStub.buildCustomerEntity(2, "Thomas", "70265996074", LocalDate.parse("1995-06-18")));

        Exchange result = producerTemplate.send("direct:getCustomers", exchange);

        CustomerEntity originCustomerEntity = result.getProperty("originCustomerEntity", CustomerEntity.class);
        assertNotNull(originCustomerEntity);
        assertEquals("19775967031", originCustomerEntity.getTaxId());
        CustomerEntity destinyCustomerEntity = result.getProperty("destinyCustomerEntity", CustomerEntity.class);
        assertNotNull(destinyCustomerEntity);
        assertEquals("70265996074", destinyCustomerEntity.getTaxId());
    }
}
