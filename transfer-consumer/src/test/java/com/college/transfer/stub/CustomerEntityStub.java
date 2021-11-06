package com.college.transfer.stub;

import com.college.transfer.entities.CustomerEntity;

import java.time.LocalDate;
import java.util.Optional;

public class CustomerEntityStub {

    public Optional<CustomerEntity> buildCustomerEntity(Integer id, String name, String taxId, LocalDate birthday) {
        return Optional.of(buildCustomer(id, name, taxId, birthday));
    }

    public CustomerEntity buildCustomer(Integer id, String name, String taxId, LocalDate birthday) {
        return CustomerEntity.builder()
                .id(id)
                .name(name)
                .taxId(taxId)
                .birthday(birthday)
                .build();
    }
}
