package com.moneymanager.dto;

import com.moneymanager.model.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    
    @NotNull(message = "Transaction type is required")
    private Transaction.TransactionType type;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Category is required")
    private String category;
    
    @NotNull(message = "Division is required")
    private Transaction.Division division;
    
    @NotNull(message = "Description is required")
    private String description;
    
    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;
    
    private String fromAccount;
    private String toAccount;
}
