package com.moneymanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type; // INCOME, EXPENSE, TRANSFER
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Division division; // OFFICE, PERSONAL
    
    @Column(nullable = false, length = 500)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "from_account")
    private String fromAccount;
    
    @Column(name = "to_account")
    private String toAccount;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private Boolean isEditable = true;
    
    public enum TransactionType {
        INCOME, EXPENSE, TRANSFER
    }
    
    public enum Division {
        OFFICE, PERSONAL
    }
}
