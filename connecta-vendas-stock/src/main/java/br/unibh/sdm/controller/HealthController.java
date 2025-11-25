package br.unibh.sdm.controller;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;

@RestController
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }
    
    @GetMapping("/health/rds")
    public Map<String, Object> healthRds() {
        Map<String, Object> rds = new HashMap<>();
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT 1");
                ResultSet rs = ps.executeQuery()
            ) {

            if (rs.next()) {
                rds.put("status", "connected");
                rds.put("validation", rs.getInt(1));
            } else {
                rds.put("status", "unexpected");
            }
        } catch (SQLException e) {
            logger.error("RDS health check failed", e);
            rds.put("status", "error");
            rds.put("error", e.getClass().getSimpleName());
            rds.put("message", e.getMessage());
        }

        return rds;
    }
}