package com.college.transfer.stub;

import com.college.transfer.model.TransferModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransferModelStub {

    public TransferModel buildTransferModel(String amount, String originAccount, String destinyAccount){
        TransferModel transferModel = new TransferModel();
        transferModel.setTransferDateTime(ZonedDateTime.parse("2019-05-01T15:35:37.599Z"));
        transferModel.setAmount(new BigDecimal(amount));
        transferModel.setOriginAccountNumber(originAccount);
        transferModel.setDestinyAccountNumber(destinyAccount);
        return transferModel;
    }
}
