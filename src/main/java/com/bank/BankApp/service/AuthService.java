package com.bank.BankApp.service;

import java.util.Optional;
import java.util.Random; // Random class generate karne ke liye account no.
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bank.BankApp.DTO.LoginRequest;
import com.bank.BankApp.DTO.RegisterRequest;
import com.bank.BankApp.entity.User;
import com.bank.BankApp.entity.Account; // Account Entity import kiya
import com.bank.BankApp.repository.UserRepository;
import com.bank.BankApp.repository.AccountRepository; // Account Repository import kiya

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository; // Inject kiya

    public AuthService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository; // Assign kiya
    }

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        // Step 1: User data create aur save karo
        User user = new User(request.getName(), request.getEmail(), request.getPassword());
        User savedUser = userRepository.save(user);

        // Step 2: Automatically unique account number generate karo
        String accountNumber = "ACC" + (100000 + new Random().nextInt(900000));
        
        // Initial balance aap 0.0 ya minimum balance jo chahein rakh sakte hain
        Double initialBalance = 0.0; 

        // Step 3: Account create karke save karo database me
        Account account = new Account(accountNumber, initialBalance, savedUser);
        accountRepository.save(account);

        return "User Registered and Account Created Successfully";
    }

    public String login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent() && user.get().getPassword().equals(request.getPassword())) {
            return "Login Successful";
        }

        return "Invalid Credentials";
    }
}

