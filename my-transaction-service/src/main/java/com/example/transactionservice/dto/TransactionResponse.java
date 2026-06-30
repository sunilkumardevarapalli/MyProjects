package com.example.transactionservice.dto;

import com.example.transactionservice.model.TransactionStatus;

public class TransactionResponse {
    private String transactionId;
    private String status;
    private String message;

    public TransactionResponse(String transactionId, String status, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
    }

    public TransactionResponse(Long id, TransactionStatus status2) {
           }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}