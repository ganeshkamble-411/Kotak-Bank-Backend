package com.bank.BankApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.BankApp.entity.Transaction;
import com.bank.BankApp.repository.TransactionRepository;

@RestController
@RequestMapping("/api/transfer")
@CrossOrigin(origins = "http://localhost:4200")
public class TransferController {

    private final TransactionRepository repository;

    public TransferController(TransactionRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody Transaction transaction) {
        transaction.setStatus("Success");
        repository.save(transaction);
        return ResponseEntity.ok("Money Transferred Successfully");
    }
}

