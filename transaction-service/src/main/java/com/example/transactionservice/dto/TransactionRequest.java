package com.example.transactionservice.dto;

public record TransactionRequest(
        String transactionId,
        long accountId,
        String accountType,
        double amount
) {
}
