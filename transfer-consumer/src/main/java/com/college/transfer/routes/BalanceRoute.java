package com.college.transfer.routes;

import com.college.transfer.entities.AccountEntity;
import com.college.transfer.model.TransferModel;
import com.college.transfer.repositories.AccountRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceRoute extends RouteBuilder {

    private final AccountRepository accountRepository;

    @Autowired
    public BalanceRoute(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void configure() {
        from("direct:transferAmountBetweenAccounts")
                .validate(this::isValidTransfer)
                .process(this::transferAmount)
                .process(this::updateAccounts)
                .end();
    }

    private Boolean isValidTransfer(Exchange exchange) {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        AccountEntity originAccount = exchange.getProperty("originAccount", AccountEntity.class);
        return originAccount.getBalance().subtract(transferModel.getAmount()).compareTo(BigDecimal.ZERO) >= 0;
    }

    private void transferAmount(Exchange exchange) {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        AccountEntity originAccount = exchange.getProperty("originAccount", AccountEntity.class);
        AccountEntity destinyAccount = exchange.getProperty("destinyAccount", AccountEntity.class);

        originAccount.setBalance(originAccount.getBalance().subtract(transferModel.getAmount()));
        destinyAccount.setBalance(destinyAccount.getBalance().add(transferModel.getAmount()));

        exchange.setProperty("originAccount", originAccount);
        exchange.setProperty("destinyAccount", destinyAccount);
    }

    private void updateAccounts(Exchange exchange) {
        AccountEntity originAccount = exchange.getProperty("originAccount", AccountEntity.class);
        AccountEntity destinyAccount = exchange.getProperty("destinyAccount", AccountEntity.class);

        accountRepository.save(originAccount);
        accountRepository.save(destinyAccount);
    }
}
