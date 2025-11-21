package br.unibh.sdm.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

@RestController
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }
    
    @GetMapping("/health/dynamodb")
    public Map<String, Object> healthDynamoDB() {
        Map<String, Object> response = new HashMap<>();
        try {
            ListTablesRequest req = new ListTablesRequest().withLimit(5);
            ListTablesResult tables = amazonDynamoDB.listTables(req);
            response.put("status", "connected");
            response.put("tables", tables.getTableNames());
            response.put("tableCount", tables.getTableNames() == null ? 0 : tables.getTableNames().size());
        } catch (Exception e) {
            logger.error("DynamoDB health check failed", e);
            response.put("status", "error");
            response.put("message", "could not connect to dynamodb");
            response.put("error", e.getClass().getSimpleName());
        }
        return response;
    }
}