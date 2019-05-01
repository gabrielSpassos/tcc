package com.college.transfer.stub;

import com.college.transfer.controllers.dto.TransferDTO;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransferDTOStub {

    public TransferDTO buildTransferDTO(BigDecimal amount, String originAccount, String destinyAccount) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setAmount(amount);
        transferDTO.setOriginAccountNumber(originAccount);
        transferDTO.setDestinyAccountNumber(destinyAccount);
        transferDTO.setTransferDateTime(ZonedDateTime.now());
        return transferDTO;
    }
}
