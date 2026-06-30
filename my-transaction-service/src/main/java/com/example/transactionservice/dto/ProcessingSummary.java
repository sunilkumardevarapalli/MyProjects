package com.example.transactionservice.dto;

public class ProcessingSummary {
    private int processedCount;
    private int failedCount;
    private int duplicateCount;

    public ProcessingSummary(int processedCount, int failedCount, int duplicateCount) {
        this.processedCount = processedCount;
        this.failedCount = failedCount;
        this.duplicateCount = duplicateCount;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(int processedCount) {
        this.processedCount = processedCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public int getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(int duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    @Override
    public String toString() {
        return "ProcessingSummary{" +
                "processedCount=" + processedCount +
                ", failedCount=" + failedCount +
                ", duplicateCount=" + duplicateCount +
                '}';
    }
}