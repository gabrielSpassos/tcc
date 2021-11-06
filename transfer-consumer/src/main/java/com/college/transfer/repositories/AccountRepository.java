package com.college.transfer.repositories;

import com.college.transfer.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

    AccountEntity findByNumber(String number);
}
