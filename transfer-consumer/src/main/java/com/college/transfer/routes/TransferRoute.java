package com.college.transfer.routes;

import com.college.transfer.entities.AccountEntity;
import com.college.transfer.entities.CustomerEntity;
import com.college.transfer.entities.TransferEntity;
import com.college.transfer.factories.TransferEntityFactory;
import com.college.transfer.model.TransferModel;
import com.college.transfer.repositories.TransferRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Component
public class TransferRoute extends RouteBuilder {

    private final TransferEntityFactory transferEntityFactory;
    private final TransferRepository transferRepository;

    @Autowired
    public TransferRoute(TransferEntityFactory transferEntityFactory, TransferRepository transferRepository) {
        this.transferEntityFactory = transferEntityFactory;
        this.transferRepository = transferRepository;
    }

    @Override
    public void configure() {
        from("direct:transferAmount")
                .routeId("transferAmount")
                .process(this::fetchOriginAccountNumber)
                .to("direct:getAccount")
                .validate(this::isOriginAccountExistent)
                .process(this::fetchDestinyAccountNumber)
                .to("direct:getAccount")
                .validate(this::isDestinyAccountExistent)
                .to("direct:getCustomers")
                .to("direct:transferAmountBetweenAccounts")
                .process(this::buildTransferEntity)
                .process(this::saveTransfer)
                .end();

    }

    private void fetchOriginAccountNumber(Exchange exchange) {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        exchange.setProperty("accountNumber", transferModel.getOriginAccountNumber());
    }

    private Boolean isOriginAccountExistent(Exchange exchange) {
        return isAccountExistent("originAccount", exchange);
    }

    private void fetchDestinyAccountNumber(Exchange exchange) {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        exchange.setProperty("accountNumber", transferModel.getDestinyAccountNumber());
    }

    private Boolean isDestinyAccountExistent(Exchange exchange) {
        return isAccountExistent("destinyAccount", exchange);
    }

    private Boolean isAccountExistent(String accountType, Exchange exchange) {
        AccountEntity accountEntity = exchange.getProperty("accountEntity", AccountEntity.class);
        if(Objects.isNull(accountEntity)) {
            return false;
        }
        exchange.setProperty(accountType, accountEntity);
        return true;
    }

    private void buildTransferEntity(Exchange exchange) {
        TransferModel transferModel = exchange.getIn().getBody(TransferModel.class);
        AccountEntity originAccount = exchange.getProperty("originAccount", AccountEntity.class);
        CustomerEntity originCustomer = exchange.getProperty("originCustomerEntity", CustomerEntity.class);
        AccountEntity destinyAccount = exchange.getProperty("destinyAccount", AccountEntity.class);
        CustomerEntity destinyCustomer = exchange.getProperty("destinyCustomerEntity", CustomerEntity.class);

        TransferEntity transferEntity = transferEntityFactory.buildTransferEntity(
                originCustomer.getId(),
                originAccount.getId(),
                destinyCustomer.getId(),
                destinyAccount.getId(),
                ZonedDateTime.now(),
                transferModel.getAmount(),
                getCurrentDate()
        );
        exchange.setProperty("transferEntity", transferEntity);
    }

    private void saveTransfer(Exchange exchange) {
        TransferEntity transferEntity = exchange.getProperty("transferEntity", TransferEntity.class);
        transferRepository.save(transferEntity);
    }

    private ZonedDateTime getCurrentDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }
}
