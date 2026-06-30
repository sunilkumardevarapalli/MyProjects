package com.example.transactionservice.service;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.exception.DuplicateTransactionException;
import com.example.transactionservice.exception.TransactionNotFoundException;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.model.TransactionStatus;
import com.example.transactionservice.processor.TransactionProcessor;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProcessor transactionProcessor;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, TransactionProcessor transactionProcessor) {
        this.transactionRepository = transactionRepository;
        this.transactionProcessor = transactionProcessor;
    }

    @Transactional
    public TransactionResponse submitTransaction(TransactionRequest request) {
        if (transactionRepository.existsById(request.getTransactionId())) {
            throw new DuplicateTransactionException("Transaction with ID " + request.getTransactionId() + " already exists.");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        
        transactionRepository.save(transaction);
        
        transactionProcessor.processTransaction(transaction);
        
        return new TransactionResponse(transaction.getId(), transaction.getStatus());
    }

    public TransactionResponse getTransactionStatus(String transactionId) {
        Optional<Transaction> transaction = Optional.ofNullable(transactionRepository.findByTransactionId(transactionId));
        if (transaction.isPresent()) {
            return new TransactionResponse(transaction.get().getId(), transaction.get().getStatus());
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionId + " not found.");
        }
    }
}