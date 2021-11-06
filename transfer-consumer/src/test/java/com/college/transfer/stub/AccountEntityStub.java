package com.college.transfer.stub;

import com.college.transfer.entities.AccountEntity;

import java.math.BigDecimal;

public class AccountEntityStub {

    public AccountEntity buildAccountEntity(Integer id, Integer customerId, String number, BigDecimal balance) {
        return AccountEntity.builder()
                .id(id)
                .customerId(customerId)
                .number(number)
                .balance(balance)
                .build();
    }
}
