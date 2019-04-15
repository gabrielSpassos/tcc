package com.college.transfer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EnableAutoConfiguration
@Entity(name="TRANSFER")
public class TransferEntity {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ORIGIN_CUSTOMER_ID", nullable = false)
    private Integer originCustomerId;
    @Column(name = "DESTINY_CUSTOMER_ID", nullable = false)
    private Integer destinyCustomerId;
    @Column(name = "ORIGIN_ACCOUNT_ID", nullable = false)
    private Integer originAccountId;
    @Column(name = "DESTINY_ACCOUNT_ID", nullable = false)
    private Integer destinyAccountId;
    @Column(name = "REGISTER_DATETIME")
    private ZonedDateTime registerDate;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
