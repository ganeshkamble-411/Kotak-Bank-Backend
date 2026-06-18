package com.bank.BankApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.BankApp.DTO.AddMoneyRequest;
import com.bank.BankApp.DTO.DashboardResponse;
import com.bank.BankApp.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Path Variable ke sath get dashboard API taaki alag-alag users ka data mile
    @GetMapping("/{accountId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long accountId) {
        DashboardResponse response = dashboardService.getDashboard(accountId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-money")
    public ResponseEntity<String> addMoney(@RequestBody AddMoneyRequest request) {
        dashboardService.addMoney(request);
        return ResponseEntity.ok("Money Added Successfully");
    }
}

