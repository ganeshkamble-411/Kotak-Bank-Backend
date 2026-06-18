package com.bank.BankApp.controller;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.BankApp.DTO.LoginRequest;
import com.bank.BankApp.DTO.RegisterRequest;
import com.bank.BankApp.entity.Account;
import com.bank.BankApp.entity.User;
import com.bank.BankApp.repository.AccountRepository;
import com.bank.BankApp.repository.UserRepository;
import com.bank.BankApp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AuthController(AuthService authService, UserRepository userRepository, AccountRepository accountRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String response = authService.login(request);
        
        if (response.contains("Success") || response.contains("successful")) {
            // 1. Email ke basis par user ko database se nikalo
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // 2. User ke ID se uska automatic bana hua account dhoondo
                Optional<Account> accountOpt = accountRepository.findByUserId(user.getId());
                Long accountId = accountOpt.isPresent() ? accountOpt.get().getId() : null;

                // 3. Ab map ke andar accountId aur userId dono daal kar return karo
                return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", response,
                    "userId", user.getId(),
                    "accountId", accountId // Yeh sabse important hai!
                ));
            }
        }
        return ResponseEntity.status(401).body(Map.of("status", "FAILED", "message", response));
    }
}



