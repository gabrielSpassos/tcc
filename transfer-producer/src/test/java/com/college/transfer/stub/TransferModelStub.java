package com.college.transfer.stub;

import com.college.transfer.model.TransferModel;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransferModelStub {

    public TransferModel buildTransferModel() {
        TransferModel transferModel = new TransferModel();
        transferModel.setAmount(BigDecimal.TEN);
        transferModel.setOriginAccountNumber("001");
        transferModel.setDestinyAccountNumber("002");
        transferModel.setTransferDateTime(ZonedDateTime.now());
        return transferModel;
    }

    public TransferModel buildTransferModel(String amount, String originAccount, String destinyAccount) {
        TransferModel transferModel = new TransferModel();
        transferModel.setAmount(new BigDecimal(amount));
        transferModel.setOriginAccountNumber(originAccount);
        transferModel.setDestinyAccountNumber(destinyAccount);
        transferModel.setTransferDateTime(ZonedDateTime.now());
        return transferModel;
    }
}
