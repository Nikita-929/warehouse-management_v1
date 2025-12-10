package com.warehouse.service;

import com.warehouse.dto.TransactionDTO;
import com.warehouse.entity.Transaction;
import com.warehouse.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    // Get all transactions
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get transactions with filters
    public Page<TransactionDTO> getTransactionsWithFilters(
            String type, String materialType, LocalDateTime startDate, 
            LocalDateTime endDate, String party, String productName, 
            int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findTransactionsWithFilters(
                type, materialType, startDate, endDate, party, productName, pageable);
        
        return transactions.map(this::convertToDTO);
    }
    
    // Get transactions for export
    public List<TransactionDTO> getTransactionsForExport(
            String type, String materialType, LocalDateTime startDate, 
            LocalDateTime endDate, String party, String productName) {
        
        return transactionRepository.findTransactionsForExport(
                type, materialType, startDate, endDate, party, productName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Save a single transaction
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = convertToEntity(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }
    
    // Save multiple transactions (for Excel upload)
    public List<TransactionDTO> saveTransactions(List<TransactionDTO> transactionDTOs) {
        List<Transaction> transactions = transactionDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        
        List<Transaction> savedTransactions = transactionRepository.saveAll(transactions);
        return savedTransactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get transaction by ID
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return convertToDTO(transaction);
    }
    
    // Delete transaction by ID
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
    
    // Convert Entity to DTO
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setBarcode(transaction.getBarcode());
        dto.setProductCode(transaction.getProductCode());
        dto.setProductName(transaction.getProductName());
        dto.setQuantity(transaction.getQuantity());
        dto.setUnit(transaction.getUnit());
        dto.setBatchNo(transaction.getBatchNo());
        dto.setGrnNo(transaction.getGrnNo());
        dto.setMaterialType(transaction.getMaterialType());
        dto.setType(transaction.getType());
        dto.setParty(transaction.getParty());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
    
    // Convert DTO to Entity
    private Transaction convertToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setBarcode(dto.getBarcode());
        transaction.setProductCode(dto.getProductCode());
        transaction.setProductName(dto.getProductName());
        transaction.setQuantity(dto.getQuantity());
        transaction.setUnit(dto.getUnit());
        transaction.setBatchNo(dto.getBatchNo());
        transaction.setGrnNo(dto.getGrnNo());
        transaction.setMaterialType(dto.getMaterialType());
        transaction.setType(dto.getType());
        transaction.setParty(dto.getParty());
        if (dto.getCreatedAt() != null) {
            transaction.setCreatedAt(dto.getCreatedAt());
        }
        return transaction;
    }
}

