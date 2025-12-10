package com.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "barcode")
    private String barcode;
    
    @NotBlank(message = "Product code is required")
    @Column(name = "product_code", nullable = false)
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @NotNull(message = "Quantity is required")
    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @NotBlank(message = "Unit is required")
    @Column(name = "unit", nullable = false)
    private String unit;
    
    @Column(name = "batch_no")
    private String batchNo;
    
    @Column(name = "grn_no")
    private String grnNo;
    
    @Pattern(regexp = "^(RM|PM|FM)$", message = "Material type must be RM, PM, or FM")
    @Column(name = "material_type", nullable = false)
    private String materialType;
    
    @Pattern(regexp = "^(IN|OUT)$", message = "Type must be IN or OUT")
    @Column(name = "type", nullable = false)
    private String type;
    
    @NotBlank(message = "Party is required")
    @Column(name = "party", nullable = false)
    private String party;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public Transaction() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Transaction(String barcode, String productCode, String productName, 
                     BigDecimal quantity, String unit, String batchNo, 
                     String grnNo, String materialType, String type, String party) {
        this();
        this.barcode = barcode;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.batchNo = batchNo;
        this.grnNo = grnNo;
        this.materialType = materialType;
        this.type = type;
        this.party = party;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getBatchNo() {
        return batchNo;
    }
    
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    
    public String getGrnNo() {
        return grnNo;
    }
    
    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo;
    }
    
    public String getMaterialType() {
        return materialType;
    }
    
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getParty() {
        return party;
    }
    
    public void setParty(String party) {
        this.party = party;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

