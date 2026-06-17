package com.bank.BankApp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bank.BankApp.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    // User ke ID se Account nikalne ke liye yeh custom method zaroori hai
    Optional<Account> findByUserId(Long userId);
}

