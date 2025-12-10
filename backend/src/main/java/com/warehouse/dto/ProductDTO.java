package com.warehouse.dto;

import java.time.LocalDateTime;

import com.warehouse.entity.MaterialType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {
    
    private Long id;
    
    @NotBlank(message = "Product code is required")
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    @DecimalMin(value = "0.0", message = "Packets must be non-negative")
    private Double packets = 0.0;
    
    @DecimalMin(value = "0.0", message = "Quantity per packet must be non-negative")
    private Double qtyPerPacket = 0.0;
    
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", message = "Quantity must be non-negative")
    private Double quantity;
    
    @NotBlank(message = "Unit is required")
    private String unit;
    
    private String batchNo;
    
    private String grnNo;
    
    private String salesInvoiceNo;
    
    @NotNull(message = "Material type is required")
    private MaterialType materialType;
    
    @NotBlank(message = "Source is required")
    private String source;
    
    private LocalDateTime dateAdded;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public ProductDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Double getPackets() {
        return packets;
    }
    
    public void setPackets(Double packets) {
        this.packets = packets;
    }
    
    public Double getQtyPerPacket() {
        return qtyPerPacket;
    }
    
    public void setQtyPerPacket(Double qtyPerPacket) {
        this.qtyPerPacket = qtyPerPacket;
    }
    
    public Double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Double quantity) {
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
    
    public String getSalesInvoiceNo() {
        return salesInvoiceNo;
    }
    
    public void setSalesInvoiceNo(String salesInvoiceNo) {
        this.salesInvoiceNo = salesInvoiceNo;
    }
    
    public MaterialType getMaterialType() {
        return materialType;
    }
    
    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
