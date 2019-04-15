package com.college.transfer.routes;

import com.college.transfer.entities.AccountEntity;
import com.college.transfer.repositories.AccountRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRoute extends RouteBuilder {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountRoute(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void configure() {
        from("direct:getAccount")
                .routeId("getAccount")
                .process(this::getAccountByNumber)
                .end();
    }

    private void getAccountByNumber(Exchange exchange) {
        String accountNumber= exchange.getProperty("accountNumber", String.class);
        AccountEntity accountEntity = accountRepository.findByNumber(accountNumber);
        exchange.setProperty("accountEntity", accountEntity);
    }
}
