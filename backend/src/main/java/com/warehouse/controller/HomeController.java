package com.warehouse.controller;

import com.warehouse.dto.ApiResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController {
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${server.port:8080}")
    private int serverPort;
    @Autowired
    private ApplicationContext applicationContext;
    
    @GetMapping
    public ResponseEntity<ApiResponse<String>> home() {
        return ResponseEntity.ok(ApiResponse.success("Warehouse Management System API", "Welcome to Warehouse Management System"));
    }
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("System is healthy"));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Object>> status() {
        boolean staticIndexExists = new ClassPathResource("static/index.html").exists();
        String userHome = System.getProperty("user.home");
        File dbFile = new File(userHome + File.separator + ".warehouse" + File.separator + "warehouse.db");
        boolean dbExists = dbFile.exists();
        boolean dbConnectOk = true;
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
        } catch (Exception e) {
            dbConnectOk = false;
        }
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("backendUp", true);
        int port = serverPort;
        try {
            if (applicationContext instanceof org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext ctx) {
                port = ctx.getWebServer().getPort();
            }
        } catch (Exception ignored) {}
        payload.put("port", port);
        payload.put("staticIndexExists", staticIndexExists);
        payload.put("dbPath", dbFile.getAbsolutePath());
        payload.put("dbExists", dbExists);
        payload.put("dbConnectOk", dbConnectOk);
        return ResponseEntity.ok(ApiResponse.success(payload));
    }
}
