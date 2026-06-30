package com.example.transactionservice.dto;

import com.example.transactionservice.model.TransactionStatus;

public record TransactionResponse(
        Long id,
        String transactionId,
        long accountId,
        String accountType,
        double amount,
        TransactionStatus status,
        long createdAt,
        long updatedAt,
        String message
) {
}
