package com.warehouse.repository;

import com.warehouse.entity.ProductName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductNameRepository extends JpaRepository<ProductName, Long> {
    
    List<ProductName> findByNameContainingIgnoreCase(String name);
    
    boolean existsByName(String name);
}
