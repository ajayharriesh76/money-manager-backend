package com.moneymanager.service;

import com.moneymanager.dto.AccountDTO;
import com.moneymanager.exception.ResourceNotFoundException;
import com.moneymanager.model.Account;
import com.moneymanager.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Transactional
    public AccountDTO createAccount(String accountName, BigDecimal initialBalance, Account.AccountType accountType) {
        Account account = new Account();
        account.setAccountName(accountName);
        account.setBalance(initialBalance);
        account.setAccountType(accountType);
        
        Account savedAccount = accountRepository.save(account);
        return convertToDTO(savedAccount);
    }
    
    @Transactional
    public void updateBalance(String accountName, BigDecimal amount) {
        Account account = accountRepository.findByAccountName(accountName)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountName));
        
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }
    
    public AccountDTO getAccountByName(String accountName) {
        Account account = accountRepository.findByAccountName(accountName)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountName));
        return convertToDTO(account);
    }
    
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        accountRepository.delete(account);
    }
    
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountName(account.getAccountName());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }
}
