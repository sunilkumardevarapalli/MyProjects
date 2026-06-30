package com.example.transactionservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transactionservice.dto.BatchTransactionResponse;
import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.model.TransactionRecord;
import com.example.transactionservice.service.TransactionProcessor;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionProcessor transactionProcessor;

    @Autowired
    public TransactionController(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionProcessor.processTransaction(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchTransactionResponse> createTransactionBatch(@RequestBody List<TransactionRequest> requests) {
        BatchTransactionResponse response = transactionProcessor.processTransactionBatch(requests);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionRecord> getTransaction(@PathVariable Long id) {
        TransactionRecord transactionRecord = transactionProcessor.getTransactionById(id);
        return transactionRecord != null ? ResponseEntity.ok(transactionRecord) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionRecord>> getAllTransactions() {
        List<TransactionRecord> transactions = transactionProcessor.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}