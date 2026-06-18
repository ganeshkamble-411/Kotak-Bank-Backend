package com.bank.BankApp.DTO;


public class AddMoneyRequest {

    private Long accountId; // Yeh new field add karni hai compiler error hatane ke liye
    private Double amount;

    // Default Constructor
    public AddMoneyRequest() {
    }

    // Parameterized Constructor
    public AddMoneyRequest(Long accountId, Double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    // Getter and Setter for accountId (Isi ki wajah se error aa rahi thi)
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    // Getter and Setter for amount
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

