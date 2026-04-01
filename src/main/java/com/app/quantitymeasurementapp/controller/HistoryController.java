package com.app.quantitymeasurementapp.controller;

import com.app.quantitymeasurementapp.dto.HistoryResponseDTO;
import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    private UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ResponseEntity<List<HistoryResponseDTO>> getAllHistory(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(historyService.getUserHistory(getCurrentUser(), limit));
    }

    @GetMapping("/operation/{operationType}")
    public ResponseEntity<List<HistoryResponseDTO>> getHistoryByOperation(
            @PathVariable String operationType) {
        return ResponseEntity.ok(historyService.getUserHistoryByOperation(getCurrentUser(), operationType));
    }

    @GetMapping("/type/{measurementType}")
    public ResponseEntity<List<HistoryResponseDTO>> getHistoryByMeasurementType(
            @PathVariable String measurementType) {
        return ResponseEntity.ok(historyService.getUserHistoryByMeasurementType(getCurrentUser(), measurementType));
    }

    @GetMapping("/errors")
    public ResponseEntity<List<HistoryResponseDTO>> getErrorHistory() {
        return ResponseEntity.ok(historyService.getUserErrorHistory(getCurrentUser()));
    }

    @GetMapping("/count/{operationType}")
    public ResponseEntity<Map<String, Object>> getOperationCount(@PathVariable String operationType) {
        Map<String, Object> response = new HashMap<>();
        response.put("operationType", operationType);
        response.put("count", historyService.getOperationCount(getCurrentUser(), operationType));
        return ResponseEntity.ok(response);
    }
}