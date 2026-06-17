package com.bank.BankApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.BankApp.entity.ContactMessage;
import com.bank.BankApp.repository.ContactRepository;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactController {

    private final ContactRepository repository;

    public ContactController(ContactRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> saveMessage(@RequestBody ContactMessage message) {
        repository.save(message); // Is line se save tabhi hoga jab entity me @Id primary key config sahi hogi
        return ResponseEntity.ok("Message Sent Successfully");
    }
}

