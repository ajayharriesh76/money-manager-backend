package com.moneymanager.dto;

import com.moneymanager.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private String category;
    private Transaction.Division division;
    private String description;
    private LocalDateTime transactionDate;
    private String fromAccount;
    private String toAccount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isEditable;
}
