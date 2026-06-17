package com.bank.BankApp.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.BankApp.DTO.DepositRequest;
import com.bank.BankApp.DTO.TransferRequest;
import com.bank.BankApp.DTO.WithdrawRequest;
import com.bank.BankApp.entity.Transaction;
import com.bank.BankApp.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest request) {
        String result = transactionService.deposit(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request) {
        String result = transactionService.withdraw(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        String result = transactionService.transfer(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Transaction>> history(@PathVariable Long accountId) {
        List<Transaction> history = transactionService.getHistory(accountId);
        return ResponseEntity.ok(history);
    }
}


