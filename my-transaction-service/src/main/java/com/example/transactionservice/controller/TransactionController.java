package com.example.transactionservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> submitTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.processTransaction(transactionRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionStatus(@PathVariable String transactionId) {
        TransactionResponse response = transactionService.getTransactionStatus(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}