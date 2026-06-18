package com.bank.BankApp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bank.BankApp.DTO.DepositRequest;
import com.bank.BankApp.DTO.TransferRequest;
import com.bank.BankApp.DTO.WithdrawRequest;
import com.bank.BankApp.entity.Account;
import com.bank.BankApp.entity.Transaction;
import com.bank.BankApp.repository.AccountRepository;
import com.bank.BankApp.repository.TransactionRepository;
import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public String deposit(DepositRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);

        // ✅ Frontend matched type: CREDIT
        Transaction transaction = new Transaction("CREDIT", request.getAmount(), "SUCCESS", account);
        transactionRepository.save(transaction);

        return "Money Deposited Successfully";
    }

    @Transactional
    public String withdraw(WithdrawRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 🌟 FIXED: Normal string return karne ke bajaye Exception throw karenge 
        // taaki controller se frontend ko explicitly error status code (like 400) mile
        if (account.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient Balance in your account");
        }

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        // ✅ Frontend matched type: DEBIT
        Transaction transaction = new Transaction("DEBIT", request.getAmount(), "SUCCESS", account);
        transactionRepository.save(transaction);

        return "Money Withdrawn Successfully";
    }

    @Transactional
    public String transfer(TransferRequest request) {
        // Ek security edge case handling: Khud ke account me hi transfer na ho jaye
        if (request.getSenderAccountId().equals(request.getReceiverAccountId())) {
            throw new IllegalArgumentException("Sender and Receiver account cannot be the same");
        }

        Account sender = accountRepository.findById(request.getSenderAccountId())
                .orElseThrow(() -> new RuntimeException("Sender Account Not Found"));

        Account receiver = accountRepository.findById(request.getReceiverAccountId())
                .orElseThrow(() -> new RuntimeException("Receiver Account Not Found"));

        // 🌟 FIXED: Throwing exception instead of returning a generic message
        if (sender.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient Balance to complete this transfer");
        }

        // Processing Atomic business balancing
        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // ✅ Frontend matched types: TRANSFER_DEBIT & TRANSFER_CREDIT
        Transaction senderTxn = new Transaction("TRANSFER_DEBIT", request.getAmount(), "SUCCESS", sender);
        Transaction receiverTxn = new Transaction("TRANSFER_CREDIT", request.getAmount(), "SUCCESS", receiver);

        transactionRepository.save(senderTxn);
        transactionRepository.save(receiverTxn);

        return "Money Transferred Successfully";
    }
    
    public List<Transaction> getHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}

