package com.college.transfer.factories;

import com.college.transfer.entities.TransferEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TransferEntityFactory {

    public TransferEntity buildTransferEntity(Integer originCustomerId,
                                              Integer originAccountId,
                                              Integer destinyCustomerId,
                                              Integer destinyAccountId,
                                              ZonedDateTime registerDate) {
        return TransferEntity.builder()
                .originCustomerId(originCustomerId)
                .originAccountId(originAccountId)
                .destinyCustomerId(destinyCustomerId)
                .destinyAccountId(destinyAccountId)
                .registerDate(registerDate)
                .build();
    }
}
