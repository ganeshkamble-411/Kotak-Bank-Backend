package com.bank.BankApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.BankApp.DTO.TransferRequest;
import com.bank.BankApp.service.TransactionService;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
@CrossOrigin(origins = "http://localhost:4200")
public class TransferController {

    private final TransactionService transactionService;

    // TransactionService inject karenge repository ke bajaye
    public TransferController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            // Service method execute hoga jisme balance check aur updates transactional hain
            String result = transactionService.transfer(request);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (IllegalArgumentException ex) {
            // Insufficient balance ya invalid account hone par 400 Bad Request bhejega
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("message", "Internal Server Error"));
        }
    }
}


