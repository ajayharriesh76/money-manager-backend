package com.moneymanager.repository;

import com.moneymanager.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Transaction> findByType(Transaction.TransactionType type);
    
    List<Transaction> findByDivision(Transaction.Division division);
    
    List<Transaction> findByCategory(String category);
    
    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByTypeAndDateRange(
        @Param("type") Transaction.TransactionType type,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.division = :division AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByDivisionAndDateRange(
        @Param("division") Transaction.Division division,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.category = :category AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByCategoryAndDateRange(
        @Param("category") String category,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT t FROM Transaction t ORDER BY t.transactionDate DESC")
    List<Transaction> findAllOrderByDateDesc();
    
    @Query("SELECT DISTINCT t.category FROM Transaction t WHERE t.type = :type")
    List<String> findDistinctCategoriesByType(@Param("type") Transaction.TransactionType type);
}
