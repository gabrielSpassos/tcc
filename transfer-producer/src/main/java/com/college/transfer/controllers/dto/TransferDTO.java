package com.college.transfer.controllers.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class TransferDTO {

    @ApiModelProperty(value = "Origin account number")
    @NotNull(message = "The origin account number must be informed")
    private String originAccountNumber;
    @ApiModelProperty(value = "Destiny account number")
    @NotNull(message = "The destiny account number must be informed")
    private String destinyAccountNumber;
    @ApiModelProperty(value = "Transfer amount")
    @NotNull(message = "The amount to transfer must be informed")
    private BigDecimal amount;
    @ApiModelProperty(value = "Transfer date time zone")
    @NotNull(message = "The transfer date time zone must be informed")
    private ZonedDateTime transferDateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
