package com.app.quantity.controller;

import com.app.quantity.dto.QuantityArithmeticRequest;

import com.app.quantity.dto.QuantityCompareRequest;
import com.app.quantity.dto.QuantityConvertRequest;
import com.app.quantity.service.QuantityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/quantities")
@RequiredArgsConstructor
public class QuantityMeasurementController {

    private final QuantityService quantityService;

    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compare(@RequestBody QuantityCompareRequest request) {
        return ResponseEntity.ok(quantityService.compare(request));
    }

    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(@RequestBody QuantityConvertRequest request) {
        return ResponseEntity.ok(quantityService.convert(request));
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@RequestBody QuantityArithmeticRequest request) {
        return ResponseEntity.ok(quantityService.add(request));
    }

    @PostMapping("/subtract")
    public ResponseEntity<Map<String, Object>> subtract(@RequestBody QuantityArithmeticRequest request) {
        return ResponseEntity.ok(quantityService.subtract(request));
    }

    @PostMapping("/multiply")
    public ResponseEntity<Map<String, Object>> multiply(@RequestBody QuantityArithmeticRequest request) {
        return ResponseEntity.ok(quantityService.multiply(request));
    }

    @PostMapping("/divide")
    public ResponseEntity<Map<String, Object>> divide(@RequestBody QuantityArithmeticRequest request) {
        return ResponseEntity.ok(quantityService.divide(request));
    }

    @GetMapping("/units")
    public ResponseEntity<Map<String, Object>> getUnits() {
        return ResponseEntity.ok(quantityService.getAllUnits());
    }
}