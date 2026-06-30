package com.example.transactionservice.visibility;

import org.springframework.stereotype.Component;

@Component
public class MetricsFacade {

    public void recordTransactionProcessed() {
        // Logic to record a processed transaction
    }

    public void recordTransactionFailed() {
        // Logic to record a failed transaction
    }

    public void recordDuplicateTransaction() {
        // Logic to record a duplicate transaction
    }

    public void logProcessingSummary(int processedCount, int failedCount, int duplicateCount) {
        // Logic to log processing summary
    }

    public void logTransactionStatus(String transactionId, String status) {
        // Logic to log the status of a transaction
    }
}