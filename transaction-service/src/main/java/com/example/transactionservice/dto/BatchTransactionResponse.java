package com.example.transactionservice.dto;

import java.util.List;


public record BatchTransactionResponse(List<TransactionResponse> transactions) {
}
