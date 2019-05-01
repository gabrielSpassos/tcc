package com.college.transfer.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferDTO {

    private String originAccountNumber;
    private String destinyAccountNumber;
    private BigDecimal amount;
    //private ZonedDateTime transferDateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
