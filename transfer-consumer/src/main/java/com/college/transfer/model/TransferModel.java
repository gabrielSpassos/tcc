package com.college.transfer.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class TransferModel {

    private String originAccountNumber;
    private String destinyAccountNumber;
    private BigDecimal amount;
    private ZonedDateTime transferDateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
