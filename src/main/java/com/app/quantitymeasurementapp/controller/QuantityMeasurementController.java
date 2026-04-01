package com.app.quantitymeasurementapp.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurementapp.dto.QuantityCompareRequest;
import com.app.quantitymeasurementapp.dto.QuantityConvertRequest;
import com.app.quantitymeasurementapp.dto.QuantityArithmeticRequest;
import com.app.quantitymeasurementapp.service.QuantityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quantities")
@RequiredArgsConstructor
public class QuantityMeasurementController {

    private final QuantityService quantityService;

    @PostMapping("/compare")
    public ResponseEntity<Map<String, Object>> compare(@RequestBody QuantityCompareRequest request) {
        Map<String, Object> result = quantityService.compare(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(@RequestBody QuantityConvertRequest request) {
        Map<String, Object> result = quantityService.convert(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@RequestBody QuantityArithmeticRequest request) {
        Map<String, Object> result = quantityService.add(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract")
    public ResponseEntity<Map<String, Object>> subtract(@RequestBody QuantityArithmeticRequest request) {
        Map<String, Object> result = quantityService.subtract(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/multiply")
    public ResponseEntity<Map<String, Object>> multiply(@RequestBody QuantityArithmeticRequest request) {
        Map<String, Object> result = quantityService.multiply(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/divide")
    public ResponseEntity<Map<String, Object>> divide(@RequestBody QuantityArithmeticRequest request) {
        Map<String, Object> result = quantityService.divide(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/units")
    public ResponseEntity<Map<String, Object>> getUnits() {
        Map<String, Object> units = quantityService.getAllUnits();
        return ResponseEntity.ok(units);
    }
}