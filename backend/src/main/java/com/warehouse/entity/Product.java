package com.warehouse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product code is required")
    @Column(name = "product_code", nullable = false)
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @DecimalMin(value = "0.0", message = "Packets must be non-negative")
    @Column(name = "packets")
    private Double packets = 0.0;
    
    @DecimalMin(value = "0.0", message = "Quantity per packet must be non-negative")
    @Column(name = "qty_per_packet")
    private Double qtyPerPacket = 0.0;
    
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", message = "Quantity must be non-negative")
    @Column(name = "quantity", nullable = false)
    private Double quantity;
    
    @NotBlank(message = "Unit is required")
    @Column(name = "unit", nullable = false)
    private String unit;
    
    @Column(name = "batch_no")
    private String batchNo;
    
    @Column(name = "grn_no")
    private String grnNo;
    
    @Column(name = "sales_invoice_no")
    private String salesInvoiceNo;
    
    @NotNull(message = "Material type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private MaterialType materialType;
    
    @NotBlank(message = "Source is required")
    @Column(name = "source", nullable = false)
    private String source;
    
    @Column(name = "date_added")
    private LocalDateTime dateAdded;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Legacy column present in some databases; mapped to satisfy NOT NULL constraint
    @Column(name = "date")
    private LocalDateTime date;
    
    // Constructors
    public Product() {
        this.createdAt = LocalDateTime.now();
        this.dateAdded = LocalDateTime.now();
        this.date = LocalDateTime.now();
    }
    
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    // Helper method to calculate quantity
    public void calculateQuantity() {
        if (packets != null && qtyPerPacket != null) {
            this.quantity = packets * qtyPerPacket;
        }
    }
}
