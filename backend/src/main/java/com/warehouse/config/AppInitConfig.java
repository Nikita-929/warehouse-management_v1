package com.warehouse.config;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInitConfig {
    @Bean
    public ApplicationRunner createDbDir() {
        return args -> {
            String userHome = System.getProperty("user.home");
            File dir = new File(userHome + File.separator + ".warehouse");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dbFile = new File(dir, "warehouse.db");
            if (!dbFile.exists()) {
                try {
                    dbFile.createNewFile();
                } catch (IOException e) {
                    // SQLite will create the file on first connection if creation fails
                }
            }
        };
    }
}
