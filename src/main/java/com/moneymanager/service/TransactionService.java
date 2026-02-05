package com.moneymanager.service;

import com.moneymanager.dto.DashboardSummaryDTO;
import com.moneymanager.dto.TransactionDTO;
import com.moneymanager.dto.TransactionRequestDTO;
import com.moneymanager.exception.ResourceNotFoundException;
import com.moneymanager.exception.TransactionNotEditableException;
import com.moneymanager.model.Transaction;
import com.moneymanager.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    
    @Transactional
    public TransactionDTO createTransaction(TransactionRequestDTO requestDTO) {
        Transaction transaction = new Transaction();
        transaction.setType(requestDTO.getType());
        transaction.setAmount(requestDTO.getAmount());
        transaction.setCategory(requestDTO.getCategory());
        transaction.setDivision(requestDTO.getDivision());
        transaction.setDescription(requestDTO.getDescription());
        transaction.setTransactionDate(requestDTO.getTransactionDate());
        transaction.setFromAccount(requestDTO.getFromAccount());
        transaction.setToAccount(requestDTO.getToAccount());
        transaction.setIsEditable(true);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Update account balances
        if (requestDTO.getType() == Transaction.TransactionType.EXPENSE && requestDTO.getFromAccount() != null) {
            accountService.updateBalance(requestDTO.getFromAccount(), requestDTO.getAmount().negate());
        } else if (requestDTO.getType() == Transaction.TransactionType.INCOME && requestDTO.getToAccount() != null) {
            accountService.updateBalance(requestDTO.getToAccount(), requestDTO.getAmount());
        } else if (requestDTO.getType() == Transaction.TransactionType.TRANSFER) {
            if (requestDTO.getFromAccount() != null) {
                accountService.updateBalance(requestDTO.getFromAccount(), requestDTO.getAmount().negate());
            }
            if (requestDTO.getToAccount() != null) {
                accountService.updateBalance(requestDTO.getToAccount(), requestDTO.getAmount());
            }
        }
        
        return convertToDTO(savedTransaction);
    }
    
    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionRequestDTO requestDTO) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        
        // Check if transaction is still editable (within 12 hours)
        if (!isEditable(transaction)) {
            throw new TransactionNotEditableException("Transaction cannot be edited after 12 hours");
        }
        
        // Revert previous account balance changes
        revertAccountBalances(transaction);
        
        transaction.setType(requestDTO.getType());
        transaction.setAmount(requestDTO.getAmount());
        transaction.setCategory(requestDTO.getCategory());
        transaction.setDivision(requestDTO.getDivision());
        transaction.setDescription(requestDTO.getDescription());
        transaction.setTransactionDate(requestDTO.getTransactionDate());
        transaction.setFromAccount(requestDTO.getFromAccount());
        transaction.setToAccount(requestDTO.getToAccount());
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        // Apply new account balance changes
        applyAccountBalances(updatedTransaction);
        
        return convertToDTO(updatedTransaction);
    }
    
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        
        if (!isEditable(transaction)) {
            throw new TransactionNotEditableException("Transaction cannot be deleted after 12 hours");
        }
        
        // Revert account balance changes
        revertAccountBalances(transaction);
        
        transactionRepository.delete(transaction);
    }
    
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return convertToDTO(transaction);
    }
    
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAllOrderByDateDesc().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByType(Transaction.TransactionType type) {
        return transactionRepository.findByType(type).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByDivision(Transaction.Division division) {
        return transactionRepository.findByDivision(division).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<TransactionDTO> getTransactionsByCategory(String category) {
        return transactionRepository.findByCategory(category).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public DashboardSummaryDTO getDashboardSummary(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryWiseExpense = new HashMap<>();
        Map<String, BigDecimal> categoryWiseIncome = new HashMap<>();
        
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                totalIncome = totalIncome.add(transaction.getAmount());
                categoryWiseIncome.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add);
            } else if (transaction.getType() == Transaction.TransactionType.EXPENSE) {
                totalExpense = totalExpense.add(transaction.getAmount());
                categoryWiseExpense.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add);
            }
        }
        
        BigDecimal balance = totalIncome.subtract(totalExpense);
        
        List<TransactionDTO> recentTransactions = transactions.stream()
            .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
            .limit(10)
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return new DashboardSummaryDTO(
            totalIncome,
            totalExpense,
            balance,
            categoryWiseExpense,
            categoryWiseIncome,
            recentTransactions
        );
    }
    
    public List<String> getCategories(Transaction.TransactionType type) {
        return transactionRepository.findDistinctCategoriesByType(type);
    }
    
    private boolean isEditable(Transaction transaction) {
        LocalDateTime now = LocalDateTime.now();
        long hoursSinceCreation = ChronoUnit.HOURS.between(transaction.getCreatedAt(), now);
        return hoursSinceCreation < 12;
    }
    
    private void revertAccountBalances(Transaction transaction) {
        if (transaction.getType() == Transaction.TransactionType.EXPENSE && transaction.getFromAccount() != null) {
            accountService.updateBalance(transaction.getFromAccount(), transaction.getAmount());
        } else if (transaction.getType() == Transaction.TransactionType.INCOME && transaction.getToAccount() != null) {
            accountService.updateBalance(transaction.getToAccount(), transaction.getAmount().negate());
        } else if (transaction.getType() == Transaction.TransactionType.TRANSFER) {
            if (transaction.getFromAccount() != null) {
                accountService.updateBalance(transaction.getFromAccount(), transaction.getAmount());
            }
            if (transaction.getToAccount() != null) {
                accountService.updateBalance(transaction.getToAccount(), transaction.getAmount().negate());
            }
        }
    }
    
    private void applyAccountBalances(Transaction transaction) {
        if (transaction.getType() == Transaction.TransactionType.EXPENSE && transaction.getFromAccount() != null) {
            accountService.updateBalance(transaction.getFromAccount(), transaction.getAmount().negate());
        } else if (transaction.getType() == Transaction.TransactionType.INCOME && transaction.getToAccount() != null) {
            accountService.updateBalance(transaction.getToAccount(), transaction.getAmount());
        } else if (transaction.getType() == Transaction.TransactionType.TRANSFER) {
            if (transaction.getFromAccount() != null) {
                accountService.updateBalance(transaction.getFromAccount(), transaction.getAmount().negate());
            }
            if (transaction.getToAccount() != null) {
                accountService.updateBalance(transaction.getToAccount(), transaction.getAmount());
            }
        }
    }
    
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setCategory(transaction.getCategory());
        dto.setDivision(transaction.getDivision());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setFromAccount(transaction.getFromAccount());
        dto.setToAccount(transaction.getToAccount());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setIsEditable(isEditable(transaction));
        return dto;
    }
}
