package com.bank.BankApp.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    // 💰 DEPOSIT NODE
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        try {
            String result = transactionService.deposit(request);
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", result));
        } catch (IllegalArgumentException e) {
            // Agar account bad input validation fail kare
            return ResponseEntity.status(400).body(Map.of("status", "FAILED", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "FAILED", "message", "Internal Server Error"));
        }
    }

    // 💸 WITHDRAW NODE
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        try {
            String result = transactionService.withdraw(request);
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", result));
        } catch (IllegalArgumentException e) {
            // 🌟 FIXED: Insufficient balance hone par explicit 400 Bad Request bhejega
            return ResponseEntity.status(400).body(Map.of("status", "FAILED", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "FAILED", "message", "Internal Server Error"));
        }
    }

    // 🔄 SECURE FUND TRANSFER NODE
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            String result = transactionService.transfer(request);
            return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", result));
        } catch (IllegalArgumentException e) {
            // 🌟 FIXED: Service se aane wali validation exceptions (Insufficient Balance / Same Account) 
            // ko yahan 400 status code ke sath catch kiya. Ab string matching ki jhanjhat khatam!
            return ResponseEntity.status(400).body(Map.of("status", "FAILED", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("status", "FAILED", "message", "Internal Server Error"));
        }
    }

    // 📜 TRANSACTION HISTORY
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Transaction>> history(@PathVariable Long accountId) {
        List<Transaction> history = transactionService.getHistory(accountId);
        return ResponseEntity.ok(history);
    }
}
