package com.college.transfer.stub;

import com.college.transfer.entities.TransferEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransferEntityStub {

    public TransferEntity buildTransferEntity(Integer id,
                                              Integer originAccount,
                                              Integer originCustomer,
                                              Integer destinyAccount,
                                              Integer destinyCustomer,
                                              BigDecimal amount) {
        return TransferEntity.builder()
                .id(id)
                .originAccountId(originAccount)
                .originCustomerId(originCustomer)
                .destinyAccountId(destinyAccount)
                .destinyCustomerId(destinyCustomer)
                .amount(amount)
                .dateTime(ZonedDateTime.parse("2019-05-01T15:35:37.599Z"))
                .registerDateTime(ZonedDateTime.now())
                .build();
    }
}
