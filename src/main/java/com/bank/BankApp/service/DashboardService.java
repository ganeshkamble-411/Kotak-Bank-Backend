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

    // 1. Dynamic getDashboard method using accountId
    public DashboardResponse getDashboard(Long accountId) {
        // Kisi specific accountId ke basis par account dhoondenge
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account Not Found"));
                
        double currentBalance = account.getBalance();

        // Pura findAll() karne ke bajaye sirf is particular account ke transactions nikalenge
        List<Transaction> transactions = transactionRepo.findByAccountId(accountId);

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

    // 2. Add Money method using dynamic accountId
    @Transactional
    public void addMoney(AddMoneyRequest request) {
        // Request object se direct accountId fetch karenge (AddMoneyRequest DTO me long accountId field add kar lena)
        Account account = accountRepo.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account Not Found"));

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepo.save(account);

        Transaction transaction = new Transaction("CREDIT", request.getAmount(), "SUCCESS", account);
        transactionRepo.save(transaction);
    }
}

