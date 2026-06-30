package com.example.transactionservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.transactionservice.model.TransactionRecord;
import com.example.transactionservice.model.TransactionStatus;
import com.example.transactionservice.repository.TransactionRepository;

@SpringBootApplication
public class TransactionServiceApplication implements CommandLineRunner {

    final TransactionRepository transactionRepository;

    TransactionServiceApplication(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Transaction Service initialized.");
        TransactionRecord transactionRecord = new TransactionRecord(
                "tx-0001",
                1001L,
                "SAVINGS",
                100.0,
                TransactionStatus.PROCESSED.name(),
                System.currentTimeMillis(),
                System.currentTimeMillis());
        transactionRepository.save(transactionRecord);
    }
}