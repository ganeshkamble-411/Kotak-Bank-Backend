package com.bank.BankApp.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.BankApp.DTO.LoginRequest;
import com.bank.BankApp.DTO.RegisterRequest;
import com.bank.BankApp.DTO.ResetPasswordRequest;
import com.bank.BankApp.entity.Account;
import com.bank.BankApp.entity.User;
import com.bank.BankApp.repository.AccountRepository;
import com.bank.BankApp.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AuthService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists! Kripya doosra email use karein.");
        }

        // Only name, email, password save hoga
        User user = new User(request.getName(), request.getEmail(), request.getPassword());
        User savedUser = userRepository.save(user);

        String accountNumber = "ACC" + (100000 + new Random().nextInt(900000));
        Double initialBalance = 0.0; 

        Account account = new Account(accountNumber, initialBalance, savedUser);
        accountRepository.save(account);

        return "User Registered and Account Created Successfully";
    }

    public String login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            return "Login Successful";
        }
        return "Invalid Credentials";
    }

    // 🔑 Direct Password Reset Logic (No Security Question Check)
    @Transactional
    public String resetPasswordDirectly(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User account nahi mila!"));

        // Direct naya password set karo
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return "Password successfully reset! Ab aap login kar sakte hain.";
    }
}




