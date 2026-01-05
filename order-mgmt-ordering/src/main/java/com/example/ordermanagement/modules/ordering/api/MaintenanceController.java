package com.example.ordermanagement.modules.ordering.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/maintenance")
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class MaintenanceController {

    private final DataSource dataSource;

    @PostMapping("/reset-db")
    public ResponseEntity<?> resetDatabase() {
        log.warn("RESTORE: Database reset triggered via Maintenance API");
        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("db-reset.sql"));
            populator.execute(dataSource);
            return ResponseEntity.ok(Map.of("message", "Database reset successfully", "status", "SUCCESS"));
        } catch (Exception e) {
            log.error("Failed to reset database", e);
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage(), "status", "FAILURE"));
        }
    }
}
