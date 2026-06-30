package com.example.transactionservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.transactionservice.exception.DuplicateTransactionException;
import com.example.transactionservice.exception.IdempotencyViolationException;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.model.TransactionStatus;
import com.example.transactionservice.processor.TransactionProcessor;
import com.example.transactionservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class TransactionProcessorTests {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionProcessor transactionProcessor;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction();
        transaction.setTransactionId(123L);
        transaction.setStatus(TransactionStatus.PENDING);
    }

    @Test
    void processTransaction_success() {
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction processedTransaction = transactionProcessor.processTransaction(transaction);

        assertEquals(TransactionStatus.COMPLETED, processedTransaction.getStatus());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void processTransaction_duplicateTransaction() {
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        assertThrows(DuplicateTransactionException.class, () -> transactionProcessor.processTransaction(transaction));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void processTransaction_idempotencyViolation() {
        transaction.setStatus(TransactionStatus.COMPLETED);
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        assertThrows(IdempotencyViolationException.class, () -> transactionProcessor.processTransaction(transaction));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void processTransaction_outOfOrderRequest() {
        transaction.setStatus(TransactionStatus.FAILED);
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        Transaction processedTransaction = transactionProcessor.processTransaction(transaction);

        assertEquals(TransactionStatus.FAILED, processedTransaction.getStatus());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void processTransaction_retrySafeFailureHandling() {
        when(transactionRepository.findById(transaction.getId())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> transactionProcessor.processTransaction(transaction));
        verify(transactionRepository, never()).save(any());
    }
}