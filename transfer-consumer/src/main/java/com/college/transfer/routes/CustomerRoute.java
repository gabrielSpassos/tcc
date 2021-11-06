package com.college.transfer.routes;

import com.college.transfer.entities.AccountEntity;
import com.college.transfer.entities.CustomerEntity;
import com.college.transfer.repositories.CustomerRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerRoute extends RouteBuilder {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRoute(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void configure() throws Exception {
        from("direct:getCustomers")
                .routeId("getCustomers")
                .process(this::fetchOriginCustomer)
                .validate(this::isOriginCustomerExistent)
                .process(this::fetchDestinyCustomer)
                .validate(this::isDestinyCustomerExistent)
                .end();
    }

    private void fetchOriginCustomer(Exchange exchange) {
        AccountEntity originAccountEntity = exchange.getProperty("originAccount", AccountEntity.class);
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(originAccountEntity.getCustomerId());
        optionalCustomerEntity
                .ifPresent(customerEntity -> exchange.setProperty("originCustomerEntity", customerEntity));
    }

    private Boolean isOriginCustomerExistent(Exchange exchange) {
        CustomerEntity originCustomerEntity = exchange.getProperty("originCustomerEntity", CustomerEntity.class);
        return Optional.ofNullable(originCustomerEntity).isPresent();
    }

    private void fetchDestinyCustomer(Exchange exchange) {
        AccountEntity destinyAccountEntity = exchange.getProperty("destinyAccount", AccountEntity.class);
        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(destinyAccountEntity.getCustomerId());
        optionalCustomerEntity
                .ifPresent(customerEntity -> exchange.setProperty("destinyCustomerEntity", customerEntity));
    }

    private Boolean isDestinyCustomerExistent(Exchange exchange) {
        CustomerEntity destinyCustomerEntity = exchange.getProperty("destinyCustomerEntity", CustomerEntity.class);
        return Optional.ofNullable(destinyCustomerEntity).isPresent();
    }
}
