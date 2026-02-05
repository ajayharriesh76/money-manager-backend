package com.moneymanager.controller;

import com.moneymanager.dto.AccountDTO;
import com.moneymanager.model.Account;
import com.moneymanager.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @RequestParam String accountName,
            @RequestParam BigDecimal initialBalance,
            @RequestParam Account.AccountType accountType) {
        AccountDTO createdAccount = accountService.createAccount(accountName, initialBalance, accountType);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{accountName}")
    public ResponseEntity<AccountDTO> getAccountByName(@PathVariable String accountName) {
        AccountDTO account = accountService.getAccountByName(accountName);
        return ResponseEntity.ok(account);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
