package com.moneymanager.controller;

import com.moneymanager.dto.DashboardSummaryDTO;
import com.moneymanager.dto.TransactionDTO;
import com.moneymanager.dto.TransactionRequestDTO;
import com.moneymanager.model.Transaction;
import com.moneymanager.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO requestDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(requestDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDTO requestDTO) {
        TransactionDTO updatedTransaction = transactionService.updateTransaction(id, requestDTO);
        return ResponseEntity.ok(updatedTransaction);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByType(@PathVariable Transaction.TransactionType type) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/division/{division}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDivision(@PathVariable Transaction.Division division) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDivision(division);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCategory(@PathVariable String category) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCategory(category);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        DashboardSummaryDTO summary = transactionService.getDashboardSummary(startDate, endDate);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories(@RequestParam Transaction.TransactionType type) {
        List<String> categories = transactionService.getCategories(type);
        return ResponseEntity.ok(categories);
    }
}
