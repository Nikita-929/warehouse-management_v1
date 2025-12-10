package com.warehouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warehouse.entity.MaterialType;
import com.warehouse.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find by product code
    Optional<Product> findByProductCode(String productCode);
    
    // Find by product name
    Optional<Product> findTopByProductName(String productName);
    
    // Find by material type
    List<Product> findByMaterialType(MaterialType materialType);
    
    // Search products by name or code
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:searchTerm% OR p.productCode LIKE %:searchTerm%")
    List<Product> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    // Get distinct values for autocomplete
    @Query("SELECT DISTINCT p.productCode FROM Product p WHERE p.productCode LIKE CONCAT(:term, '%')")
    List<String> findDistinctProductCodes(@Param("term") String term);
    
    @Query("SELECT DISTINCT p.productName FROM Product p WHERE p.productName LIKE CONCAT(:term, '%')")
    List<String> findDistinctProductNames(@Param("term") String term);
    
    @Query("SELECT DISTINCT p.unit FROM Product p WHERE p.unit LIKE CONCAT(:term, '%')")
    List<String> findDistinctUnits(@Param("term") String term);
    
    @Query("SELECT DISTINCT p.batchNo FROM Product p WHERE p.batchNo IS NOT NULL AND p.batchNo LIKE CONCAT(:term, '%')")
    List<String> findDistinctBatchNos(@Param("term") String term);
    
    @Query("SELECT DISTINCT p.grnNo FROM Product p WHERE p.grnNo IS NOT NULL AND p.grnNo LIKE CONCAT(:term, '%')")
    List<String> findDistinctGrnNos(@Param("term") String term);
    
    @Query("SELECT DISTINCT p.salesInvoiceNo FROM Product p WHERE p.salesInvoiceNo IS NOT NULL AND p.salesInvoiceNo LIKE CONCAT(:term, '%')")
    List<String> findDistinctSalesInvoiceNos(@Param("term") String term);
    
    @Query("SELECT DISTINCT p.source FROM Product p WHERE p.source LIKE CONCAT(:term, '%')")
    List<String> findDistinctSources(@Param("term") String term);
}
