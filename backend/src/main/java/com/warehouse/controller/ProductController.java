package com.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.dto.ApiResponse;
import com.warehouse.dto.ProductDTO;
import com.warehouse.entity.MaterialType;
import com.warehouse.entity.Product;
import com.warehouse.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Validated
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    // Get all products
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error loading products: " + e.getMessage()));
        }
    }
    
    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        try {
            return productService.getProductById(id)
                    .map(product -> ResponseEntity.ok(ApiResponse.success(product)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error loading product: " + e.getMessage()));
        }
    }
    
    // Add new product
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            // Allow duplicate product codes by design
            
            // Convert DTO to Entity
            Product product = convertToEntity(productDTO);
            Product savedProduct = productService.saveProduct(product);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Product added successfully", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error adding product: " + e.getMessage()));
        }
    }
    
    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting product: " + e.getMessage()));
        }
    }
    
    // Search products
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestParam(required = false) String q) {
        try {
            List<Product> products = productService.searchProducts(q);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error searching products: " + e.getMessage()));
        }
    }
    
    // Filter by material type
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Product>>> filterByMaterialType(@RequestParam MaterialType materialType) {
        try {
            List<Product> products = productService.getProductsByMaterialType(materialType);
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error filtering products: " + e.getMessage()));
        }
    }
    
    // Autocomplete endpoints
    @GetMapping("/autocomplete/product-code")
    public ResponseEntity<ApiResponse<List<String>>> getProductCodeSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getProductCodeSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }
    
    @GetMapping("/autocomplete/product-name")
    public ResponseEntity<ApiResponse<List<String>>> getProductNameSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getProductNameSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }

    // Lookup product by name to autofill code
    @GetMapping("/lookup/by-name")
    public ResponseEntity<ApiResponse<Product>> getByProductName(@RequestParam String name) {
        try {
            return productService.findByProductName(name)
                    .map(p -> ResponseEntity.ok(ApiResponse.success(p)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Product not found")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error looking up product: " + e.getMessage()));
        }
    }

    // Lookup by product code to enforce one-to-one code<->name mapping
    @GetMapping("/lookup/by-code")
    public ResponseEntity<ApiResponse<Product>> getByProductCode(@RequestParam String code) {
        try {
            return productService.findByProductCode(code)
                    .map(p -> ResponseEntity.ok(ApiResponse.success(p)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Product not found")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error looking up product: " + e.getMessage()));
        }
    }
    
    @GetMapping("/autocomplete/unit")
    public ResponseEntity<ApiResponse<List<String>>> getUnitSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getUnitSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }
    
    @GetMapping("/autocomplete/batch-no")
    public ResponseEntity<ApiResponse<List<String>>> getBatchNoSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getBatchNoSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }
    
    @GetMapping("/autocomplete/grn-no")
    public ResponseEntity<ApiResponse<List<String>>> getGrnNoSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getGrnNoSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }
    
    @GetMapping("/autocomplete/sales-invoice-no")
    public ResponseEntity<ApiResponse<List<String>>> getSalesInvoiceNoSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getSalesInvoiceNoSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }
    
    @GetMapping("/autocomplete/source")
    public ResponseEntity<ApiResponse<List<String>>> getSourceSuggestions(@RequestParam String term) {
        try {
            List<String> suggestions = productService.getSourceSuggestions(term);
            return ResponseEntity.ok(ApiResponse.success(suggestions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error getting suggestions: " + e.getMessage()));
        }
    }
    
    // Helper method to convert DTO to Entity
    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductCode(dto.getProductCode());
        product.setProductName(dto.getProductName());
        product.setPackets(dto.getPackets());
        product.setQtyPerPacket(dto.getQtyPerPacket());
        product.setQuantity(dto.getQuantity());
        product.setUnit(dto.getUnit());
        product.setBatchNo(dto.getBatchNo());
        product.setGrnNo(dto.getGrnNo());
        product.setSalesInvoiceNo(dto.getSalesInvoiceNo());
        product.setMaterialType(dto.getMaterialType());
        product.setSource(dto.getSource());
        product.setDateAdded(dto.getDateAdded());
        
        return product;
    }
}
