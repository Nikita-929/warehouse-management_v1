package com.warehouse.repository;

import com.warehouse.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Find transactions by type
    List<Transaction> findByType(String type);
    
    // Find transactions by material type
    List<Transaction> findByMaterialType(String materialType);
    
    // Find transactions by party
    List<Transaction> findByPartyContainingIgnoreCase(String party);
    
    // Find transactions by date range
    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find transactions by type and date range
    List<Transaction> findByTypeAndCreatedAtBetween(String type, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find transactions by material type and date range
    List<Transaction> findByMaterialTypeAndCreatedAtBetween(String materialType, LocalDateTime startDate, LocalDateTime endDate);
    
    // Search transactions by multiple criteria
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:type IS NULL OR t.type = :type) AND " +
           "(:materialType IS NULL OR t.materialType = :materialType) AND " +
           "(:startDate IS NULL OR t.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR t.createdAt <= :endDate) AND " +
           "(:party IS NULL OR LOWER(t.party) LIKE LOWER(CONCAT('%', :party, '%'))) AND " +
           "(:productName IS NULL OR LOWER(t.productName) LIKE LOWER(CONCAT('%', :productName, '%')))")
    Page<Transaction> findTransactionsWithFilters(
            @Param("type") String type,
            @Param("materialType") String materialType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("party") String party,
            @Param("productName") String productName,
            Pageable pageable);
    
    // Get all transactions for export
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:type IS NULL OR t.type = :type) AND " +
           "(:materialType IS NULL OR t.materialType = :materialType) AND " +
           "(:startDate IS NULL OR t.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR t.createdAt <= :endDate) AND " +
           "(:party IS NULL OR LOWER(t.party) LIKE LOWER(CONCAT('%', :party, '%'))) AND " +
           "(:productName IS NULL OR LOWER(t.productName) LIKE LOWER(CONCAT('%', :productName, '%'))) " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsForExport(
            @Param("type") String type,
            @Param("materialType") String materialType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("party") String party,
            @Param("productName") String productName);
}

