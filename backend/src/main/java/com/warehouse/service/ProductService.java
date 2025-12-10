package com.warehouse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.entity.MaterialType;
import com.warehouse.entity.Product;
import com.warehouse.entity.ProductName;
import com.warehouse.repository.ProductNameRepository;
import com.warehouse.repository.ProductRepository;
import com.warehouse.repository.SupplierRepository;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductNameRepository productNameRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // Save product
    public Product saveProduct(Product product) {
        // Calculate quantity if not provided
        if (product.getQuantity() == null) {
            product.calculateQuantity();
        }
        
        // Save the product
        Product savedProduct = productRepository.save(product);
        
        // Add product name to suggestions if not exists
        if (!productNameRepository.existsByName(product.getProductName())) {
            ProductName productName = new ProductName(product.getProductName());
            productNameRepository.save(productName);
        }
        
        return savedProduct;
    }
    
    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // Check if product code exists (disabled to allow duplicate codes)
    public boolean productCodeExists(String productCode) {
        return false;
    }
    
    // Get product by name (for autofill)
    public Optional<Product> findByProductName(String productName) {
        return productRepository.findTopByProductName(productName);
    }

    // Get product by code (for autofill/validation)
    public Optional<Product> findByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }
    
    // Search products
    public List<Product> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findBySearchTerm(searchTerm.trim());
    }
    
    // Filter by material type
    public List<Product> getProductsByMaterialType(MaterialType materialType) {
        return productRepository.findByMaterialType(materialType);
    }
    
    // Autocomplete suggestions
    public List<String> getProductCodeSuggestions(String term) {
        return productRepository.findDistinctProductCodes(term);
    }
    
    public List<String> getProductNameSuggestions(String term) {
        return productRepository.findDistinctProductNames(term);
    }
    
    public List<String> getUnitSuggestions(String term) {
        return productRepository.findDistinctUnits(term);
    }
    
    public List<String> getBatchNoSuggestions(String term) {
        return productRepository.findDistinctBatchNos(term);
    }
    
    public List<String> getGrnNoSuggestions(String term) {
        return productRepository.findDistinctGrnNos(term);
    }
    
    public List<String> getSalesInvoiceNoSuggestions(String term) {
        return productRepository.findDistinctSalesInvoiceNos(term);
    }
    
    public List<String> getSourceSuggestions(String term) {
        return productRepository.findDistinctSources(term);
    }
    
    // Get all product names for dropdown
    public List<ProductName> getAllProductNames() {
        return productNameRepository.findAll();
    }
    
    // Get all suppliers
    public List<com.warehouse.entity.Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
}
