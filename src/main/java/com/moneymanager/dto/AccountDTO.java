package com.moneymanager.dto;

import com.moneymanager.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String accountName;
    private BigDecimal balance;
    private Account.AccountType accountType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
