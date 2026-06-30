package com.example.transactionservice.processor;

import com.example.transactionservice.exception.DuplicateTransactionException;
import com.example.transactionservice.exception.IdempotencyViolationException;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.model.TransactionStatus;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionProcessor {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionProcessor(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction processTransaction(Transaction transaction) {
        validateTransaction(transaction);
        transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
        
        // Simulate processing logic
        // In a real application, this would involve more complex operations
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    private void validateTransaction(Transaction transaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(transaction.getId());
        if (existingTransaction.isPresent()) {
            throw new DuplicateTransactionException("Transaction with ID " + transaction.getId() + " already exists.");
        }
        // Additional validation logic can be added here
    }
}