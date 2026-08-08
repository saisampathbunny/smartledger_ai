package com.smartledger.smartledger.controller;

import com.smartledger.smartledger.model.Transaction;
import com.smartledger.smartledger.repository.TransactionRepository;
import com.smartledger.smartledger.service.FraudDetectionService;
import com.smartledger.smartledger.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
        // inline validation — return error response instead of throwing
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            return ResponseEntity.badRequest().body("Transaction amount must be greater than zero.");
        }
        if (transaction.getSenderAccount() == null || transaction.getSenderAccount().isBlank()) {
            return ResponseEntity.badRequest().body("Sender account is required.");
        }
        if (transaction.getReceiverAccount() == null || transaction.getReceiverAccount().isBlank()) {
            return ResponseEntity.badRequest().body("Receiver account is required.");
        }

        FraudDetectionService.FraudResult result = fraudDetectionService.evaluate(transaction);
        transaction.setFraudStatus(result.status());

        String explanation = geminiService.explainFlag(transaction, result.score());
        transaction.setExplanation(explanation);

        return ResponseEntity.ok(repository.save(transaction));
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        return repository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("No transaction found with id " + id));
    }
}
