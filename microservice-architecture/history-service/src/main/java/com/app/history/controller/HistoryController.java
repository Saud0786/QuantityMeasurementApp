package com.app.history.controller;

import com.app.history.dto.HistoryResponseDTO;
import com.app.history.dto.SaveHistoryRequest;
import com.app.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/internal/history/save")
    public ResponseEntity<Void> saveHistory(@RequestBody SaveHistoryRequest request,
                                            @RequestHeader(value = "X-User-Id", required = false) Long userId,
                                            @RequestHeader(value = "X-User-Email", required = false) String userEmail) {
        
        System.out.println("📝 SAVING HISTORY - X-User-Id: " + userId);
        System.out.println("📝 SAVING HISTORY - X-User-Email: " + userEmail);
        
        // ✅ FIX: Agar userId null hai to error throw karo, default mat use karo
        if (userId == null) {
            System.err.println("❌ ERROR: userId is null! Cannot save history without user authentication.");
            throw new RuntimeException("User ID is required. Please authenticate first.");
        }
        
        if (userEmail == null) {
            userEmail = "unknown@example.com";
        }
        
        historyService.saveHistory(request, userId, userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/history")
    public ResponseEntity<List<HistoryResponseDTO>> getAllHistory(
            @RequestParam(defaultValue = "50") int limit,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        System.out.println("📋 GET HISTORY - X-User-Id: " + userId);
        
        // ✅ FIX: Agar userId null hai to unauthorized error
        if (userId == null) {
            System.err.println("❌ ERROR: userId is null! Cannot fetch history without authentication.");
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.ok(historyService.getUserHistory(userId, limit));
    }

    @GetMapping("/api/history/operation/{operationType}")
    public ResponseEntity<List<HistoryResponseDTO>> getHistoryByOperation(
            @PathVariable String operationType,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.ok(historyService.getUserHistoryByOperation(userId, operationType));
    }

    @GetMapping("/api/history/type/{measurementType}")
    public ResponseEntity<List<HistoryResponseDTO>> getHistoryByMeasurementType(
            @PathVariable String measurementType,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.ok(historyService.getUserHistoryByMeasurementType(userId, measurementType));
    }

    @GetMapping("/api/history/errors")
    public ResponseEntity<List<HistoryResponseDTO>> getErrorHistory(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.ok(historyService.getUserErrorHistory(userId));
    }

    @GetMapping("/api/history/count/{operationType}")
    public ResponseEntity<Map<String, Object>> getOperationCount(
            @PathVariable String operationType,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("operationType", operationType);
        response.put("count", historyService.getOperationCount(userId, operationType));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/history")
    public ResponseEntity<Map<String, String>> clearUserHistory(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        
        historyService.clearUserHistory(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "History cleared successfully");
        return ResponseEntity.ok(response);
    }
}