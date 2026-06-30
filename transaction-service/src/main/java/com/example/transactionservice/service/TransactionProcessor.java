package com.example.transactionservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transactionservice.dto.BatchTransactionResponse;
import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.model.TransactionRecord;
import com.example.transactionservice.model.TransactionStatus;
import com.example.transactionservice.repository.TransactionRepository;

@Service
public class TransactionProcessor {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionProcessor(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {
        return processTransactionBatch(List.of(request)).transactions().get(0);
    }

    @Transactional
    public BatchTransactionResponse processTransactionBatch(List<TransactionRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new BatchTransactionResponse(List.of());
        }

        Set<String> transactionIds = requests.stream()
                .map(TransactionRequest::transactionId)
                .collect(Collectors.toSet());

        Map<String, TransactionRecord> existingRecords = transactionRepository.findByTransactionIdIn(transactionIds).stream()
                .collect(Collectors.toMap(TransactionRecord::getTransactionId, Function.identity()));

        Set<String> seenInBatch = new HashSet<>();
        List<TransactionResponse> responses = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (TransactionRequest request : requests) {
            String idempotencyKey = request.transactionId();
            if (!seenInBatch.add(idempotencyKey)) {
                responses.add(buildDuplicateResponse(request, existingRecords.get(idempotencyKey), "Duplicate request within batch"));
                continue;
            }

            TransactionRecord existing = existingRecords.get(idempotencyKey);
            if (existing != null) {
                responses.add(buildDuplicateResponse(request, existing, "Duplicate request already processed"));
                continue;
            }

            TransactionRecord record = new TransactionRecord(
                    request.transactionId(),
                    request.accountId(),
                    request.accountType(),
                    request.amount(),
                    TransactionStatus.PROCESSED.name(),
                    now,
                    now);

            TransactionRecord saved = transactionRepository.save(record);
            existingRecords.put(saved.getTransactionId(), saved);
            responses.add(buildProcessedResponse(saved));
        }

        return new BatchTransactionResponse(responses);
    }

    public TransactionRecord getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<TransactionRecord> getAllTransactions() {
        return transactionRepository.findAll();
    }

    private TransactionResponse buildProcessedResponse(TransactionRecord record) {
        return new TransactionResponse(
                record.getId(),
                record.getTransactionId(),
                record.getAccountId(),
                record.getAccountType(),
                record.getAmount(),
                TransactionStatus.PROCESSED,
                record.getCreatedAt(),
                record.getUpdatedAt(),
                "Transaction processed successfully");
    }

    private TransactionResponse buildDuplicateResponse(TransactionRequest request, TransactionRecord existing, String message) {
        if (existing != null) {
            return new TransactionResponse(
                    existing.getId(),
                    existing.getTransactionId(),
                    existing.getAccountId(),
                    existing.getAccountType(),
                    existing.getAmount(),
                    TransactionStatus.DUPLICATE,
                    existing.getCreatedAt(),
                    existing.getUpdatedAt(),
                    message);
        }

        return new TransactionResponse(
                null,
                request.transactionId(),
                request.accountId(),
                request.accountType(),
                request.amount(),
                TransactionStatus.DUPLICATE,
                0,
                0,
                message);
    }
}
