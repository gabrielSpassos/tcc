package com.college.transfer.factories;

import com.college.transfer.entities.TransferEntity;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransferEntityFactoryTest {

    private TransferEntityFactory transferEntityFactory;

    @Before
    public void setup() {
        this.transferEntityFactory = new TransferEntityFactory();
    }

    @Test
    public void shouldBuildTransferEntity() {
        ZonedDateTime dateTime = ZonedDateTime.parse("2019-05-01T15:35:37.599Z");
        ZonedDateTime register = ZonedDateTime.parse("2019-05-01T17:35:37.599Z");
        TransferEntity transferEntity = transferEntityFactory.buildTransferEntity(
                1, 1, 2, 2, dateTime, BigDecimal.TEN, register);

        assertNotNull(transferEntity);
        assertEquals(1, transferEntity.getOriginCustomerId().intValue());
        assertEquals(1, transferEntity.getOriginAccountId().intValue());
        assertEquals(2, transferEntity.getDestinyCustomerId().intValue());
        assertEquals(2, transferEntity.getDestinyAccountId().intValue());
        assertEquals(new BigDecimal("10"), transferEntity.getAmount());
        assertNotNull(transferEntity.getRegisterDateTime());
        assertNotNull(transferEntity.getDateTime());
    }
}
