package com.bank.BankApp.service;

import java.util.List;
import java.util.stream.Collectors; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bank.BankApp.DTO.AddMoneyRequest;
import com.bank.BankApp.DTO.DashboardResponse;
import com.bank.BankApp.entity.Account;
import com.bank.BankApp.entity.Transaction;
import com.bank.BankApp.repository.AccountRepository;
import com.bank.BankApp.repository.TransactionRepository;

@Service
public class DashboardService {

    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public DashboardService(AccountRepository accountRepo, TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public DashboardResponse getDashboard() {
        // ID 1L se account check karega, agar nahi mila toh empty response/dummy set karega bina crash huye
        Account account = accountRepo.findById(1L).orElse(null);
        double currentBalance = (account != null) ? account.getBalance() : 0.0;

        List<Transaction> transactions = transactionRepo.findAll();

        double totalCredit = transactions.stream()
                .filter(t -> "CREDIT".equals(t.getType()) || "TRANSFER_CREDIT".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalDebit = transactions.stream()
                .filter(t -> "DEBIT".equals(t.getType()) || "TRANSFER_DEBIT".equals(t.getType()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        DashboardResponse response = new DashboardResponse();
        response.setTotalBalance(currentBalance);
        response.setTotalCredit(totalCredit);
        response.setTotalDebit(totalDebit);

        response.setChartData(
                transactions.stream()
                        .map(Transaction::getAmount)
                        .collect(Collectors.toList())
        );

        return response;
    }

    @Transactional
    public void addMoney(AddMoneyRequest request) {
        Account account = accountRepo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default Account Not Found"));

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepo.save(account);

        // Actual Entity constructor parameter mapping ke hisab se pass kiya hai:
        // Transaction(String type, Double amount, String status, Account account)
        Transaction transaction = new Transaction("CREDIT", request.getAmount(), "SUCCESS", account);
        transactionRepo.save(transaction);
    }
}


