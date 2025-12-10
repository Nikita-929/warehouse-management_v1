package com.warehouse.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.warehouse.dto.ApiResponse;
import com.warehouse.dto.TransactionDTO;
import com.warehouse.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAll() {
        List<TransactionDTO> list = transactionService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<UploadResult>> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is empty"));
            }

            if (!isExcelFile(file)) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Please upload an Excel file (.xlsx or .xls)"));
            }

            List<TransactionDTO> transactions = parseExcelFile(file.getInputStream());
            
            if (transactions.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No valid data found in the Excel file"));
            }

            // Save transactions to database
            List<TransactionDTO> savedTransactions = transactionService.saveTransactions(transactions);
            
            UploadResult result = new UploadResult();
            result.setProcessed(savedTransactions.size());
            result.setTotal(transactions.size());
            result.setMessage("Excel file uploaded and processed successfully");
            
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error reading file: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error processing file: " + e.getMessage()));
        }
    }

    private boolean isExcelFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"));
    }

    private List<TransactionDTO> parseExcelFile(InputStream inputStream) throws IOException {
        List<TransactionDTO> transactions = new ArrayList<>();
        
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                TransactionDTO transaction = new TransactionDTO();
                
                // Parse each column based on the expected format
                transaction.setBarcode(getCellValueAsString(row.getCell(0)));
                transaction.setProductCode(getCellValueAsString(row.getCell(1)));
                transaction.setProductName(getCellValueAsString(row.getCell(2)));
                
                // Parse quantity as number
                Cell quantityCell = row.getCell(3);
                if (quantityCell != null && quantityCell.getCellType() == CellType.NUMERIC) {
                    transaction.setQuantity(BigDecimal.valueOf(quantityCell.getNumericCellValue()));
                } else if (quantityCell != null && quantityCell.getCellType() == CellType.STRING) {
                    try {
                        transaction.setQuantity(new BigDecimal(quantityCell.getStringCellValue()));
                    } catch (NumberFormatException e) {
                        // Skip this row if quantity is not a valid number
                        continue;
                    }
                } else {
                    continue; // Skip row if quantity is missing
                }
                
                transaction.setUnit(getCellValueAsString(row.getCell(4)));
                transaction.setBatchNo(getCellValueAsString(row.getCell(5)));
                transaction.setGrnNo(getCellValueAsString(row.getCell(6)));
                transaction.setMaterialType(getCellValueAsString(row.getCell(7)));
                transaction.setType(getCellValueAsString(row.getCell(8)));
                transaction.setParty(getCellValueAsString(row.getCell(9)));
                
                // Parse date (column 10)
                Cell dateCell = row.getCell(10);
                if (dateCell != null) {
                    LocalDateTime transactionDate = parseDateFromCell(dateCell);
                    if (transactionDate != null) {
                        transaction.setCreatedAt(transactionDate);
                    } else {
                        // If date parsing fails, use current date
                        transaction.setCreatedAt(LocalDateTime.now());
                    }
                } else {
                    // If no date provided, use current date
                    transaction.setCreatedAt(LocalDateTime.now());
                }
                
                // Validate required fields
                if (transaction.getProductCode() != null && !transaction.getProductCode().trim().isEmpty() &&
                    transaction.getProductName() != null && !transaction.getProductName().trim().isEmpty() &&
                    transaction.getType() != null && !transaction.getType().trim().isEmpty()) {
                    transactions.add(transaction);
                }
            }
        }
        
        return transactions;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private LocalDateTime parseDateFromCell(Cell cell) {
        if (cell == null) return null;
        
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                // Excel date cell
                return cell.getDateCellValue().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            } else if (cell.getCellType() == CellType.STRING) {
                // String date - try to parse common formats
                String dateStr = cell.getStringCellValue().trim();
                if (dateStr.isEmpty()) return null;
                
                // Try different date formats - prioritize Indian format
                try {
                    // Try DD/MM/YYYY format (Indian format) - PRIMARY
                    if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                        String[] parts = dateStr.split("/");
                        int day = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);
                        int year = Integer.parseInt(parts[2]);
                        return LocalDateTime.of(year, month, day, 0, 0);
                    }
                    // Try DD-MM-YYYY format (Indian format alternative)
                    else if (dateStr.matches("\\d{1,2}-\\d{1,2}-\\d{4}")) {
                        String[] parts = dateStr.split("-");
                        int day = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);
                        int year = Integer.parseInt(parts[2]);
                        return LocalDateTime.of(year, month, day, 0, 0);
                    }
                    // Try YYYY-MM-DD format (ISO format) - SECONDARY
                    else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                        String[] parts = dateStr.split("-");
                        int year = Integer.parseInt(parts[0]);
                        int month = Integer.parseInt(parts[1]);
                        int day = Integer.parseInt(parts[2]);
                        return LocalDateTime.of(year, month, day, 0, 0);
                    }
                } catch (Exception e) {
                    // If parsing fails, return null
                    return null;
                }
            }
        } catch (Exception e) {
            // If any error occurs, return null
            return null;
        }
        
        return null;
    }

    // Inner class for upload result
    public static class UploadResult {
        private int processed;
        private int total;
        private String message;

        public int getProcessed() { return processed; }
        public void setProcessed(int processed) { this.processed = processed; }
        
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}


