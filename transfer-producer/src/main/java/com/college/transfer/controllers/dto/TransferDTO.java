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

    @ApiModelProperty(value = "")
    @NotNull(message = "")
    private String originAccountNumber;
    @ApiModelProperty(value = "")
    @NotNull(message = "")
    private String destinyAccountNumber;
    @ApiModelProperty(value = "")
    @NotNull(message = "")
    private BigDecimal amount;
    @ApiModelProperty(value = "")
    @NotNull(message = "")
    private ZonedDateTime transferDateTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
