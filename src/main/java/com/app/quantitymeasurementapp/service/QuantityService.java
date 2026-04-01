package com.app.quantitymeasurementapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurementapp.dto.QuantityArithmeticRequest;
import com.app.quantitymeasurementapp.dto.QuantityCompareRequest;
import com.app.quantitymeasurementapp.dto.QuantityConvertRequest;
import com.app.quantitymeasurementapp.model.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuantityService {

    private final HistoryService historyService;

    // Unit conversion factors (base unit: METER for length, KILOGRAM for weight, LITER for volume)
    private static final Map<String, Map<String, Double>> conversionFactors = new HashMap<>();

    static {
        // Length units (base: METER)
        Map<String, Double> lengthUnits = new HashMap<>();
        lengthUnits.put("METER", 1.0);
        lengthUnits.put("KILOMETER", 1000.0);
        lengthUnits.put("CENTIMETER", 0.01);
        lengthUnits.put("MILLIMETER", 0.001);
        lengthUnits.put("MILE", 1609.344);
        lengthUnits.put("YARD", 0.9144);
        lengthUnits.put("FOOT", 0.3048);
        lengthUnits.put("INCH", 0.0254);
        conversionFactors.put("LENGTH", lengthUnits);

        // Weight units (base: KILOGRAM)
        Map<String, Double> weightUnits = new HashMap<>();
        weightUnits.put("KILOGRAM", 1.0);
        weightUnits.put("GRAM", 0.001);
        weightUnits.put("MILLIGRAM", 0.000001);
        weightUnits.put("POUND", 0.453592);
        weightUnits.put("OUNCE", 0.0283495);
        conversionFactors.put("WEIGHT", weightUnits);

        // Volume units (base: LITER)
        Map<String, Double> volumeUnits = new HashMap<>();
        volumeUnits.put("LITER", 1.0);
        volumeUnits.put("MILLILITER", 0.001);
        volumeUnits.put("GALLON", 3.78541);
        volumeUnits.put("QUART", 0.946353);
        volumeUnits.put("PINT", 0.473176);
        conversionFactors.put("VOLUME", volumeUnits);
    }

    // Get current logged-in user (returns null if not authenticated)
    private UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserEntity) {
            return (UserEntity) auth.getPrincipal();
        }
        return null;
    }

    // ==================== COMPARE ====================
    public Map<String, Object> compare(QuantityCompareRequest request) {
        UserEntity user = getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        
        try {
            double value1InBase;
            double value2InBase;
            
            // ✅ Handle temperature separately
            if ("TEMPERATURE".equals(request.getType())) {
                value1InBase = convertTemperatureToBase(request.getValue1(), request.getUnit1());
                value2InBase = convertTemperatureToBase(request.getValue2(), request.getUnit2());
            } else {
                value1InBase = convertToBase(request.getValue1(), request.getUnit1(), request.getType());
                value2InBase = convertToBase(request.getValue2(), request.getUnit2(), request.getType());
            }
            
            String comparison;
            if (Math.abs(value1InBase - value2InBase) < 0.0001) {
                comparison = "equal to";
            } else if (value1InBase < value2InBase) {
                comparison = "less than";
            } else {
                comparison = "greater than";
            }
            
            String resultText = String.format("%.4f %s is %s %.4f %s", 
                request.getValue1(), request.getUnit1(), comparison, 
                request.getValue2(), request.getUnit2());
            
            result.put("success", true);
            result.put("comparison", comparison);
            result.put("message", resultText);
            
            // Save to history ONLY if user is logged in
            if (user != null) {
                historyService.saveHistory(user, "COMPARE", request.getType(),
                    request.getValue1(), request.getUnit1(), 
                    request.getValue2(), request.getUnit2(),
                    null, null, resultText, false, null);
            }
                
        } catch (Exception e) {
            if (user != null) {
                historyService.saveHistory(user, "COMPARE", request.getType(),
                    request.getValue1(), request.getUnit1(), 
                    request.getValue2(), request.getUnit2(),
                    null, null, null, true, e.getMessage());
            }
            throw e;
        }
        return result;
    }

    // ==================== CONVERT ====================
    public Map<String, Object> convert(QuantityConvertRequest request) {
        UserEntity user = getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        
        try {
            double baseValue;
            if ("TEMPERATURE".equals(request.getType())) {
                baseValue = convertTemperatureToBase(request.getValue(), request.getFromUnit());
            } else {
                baseValue = convertToBase(request.getValue(), request.getFromUnit(), request.getType());
            }
            
            double convertedValue;
            if ("TEMPERATURE".equals(request.getType())) {
                convertedValue = convertTemperatureFromBase(baseValue, request.getToUnit());
            } else {
                convertedValue = convertFromBase(baseValue, request.getToUnit(), request.getType());
            }
            
            String resultText = String.format("%.4f %s = %.4f %s", 
                request.getValue(), request.getFromUnit(), convertedValue, request.getToUnit());
            
            result.put("success", true);
            result.put("value", convertedValue);
            result.put("result", resultText);
            
            // Save to history ONLY if user is logged in
            if (user != null) {
                historyService.saveHistory(user, "CONVERT", request.getType(),
                    request.getValue(), request.getFromUnit(), 
                    null, null,
                    convertedValue, request.getToUnit(), resultText, false, null);
            }
                
        } catch (Exception e) {
            if (user != null) {
                historyService.saveHistory(user, "CONVERT", request.getType(),
                    request.getValue(), request.getFromUnit(), 
                    null, null,
                    null, null, null, true, e.getMessage());
            }
            throw e;
        }
        return result;
    }

    // ==================== ADD ====================
    public Map<String, Object> add(QuantityArithmeticRequest request) {
        // Temperature does not support arithmetic operations
        if ("TEMPERATURE".equals(request.getType())) {
            throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
        }
        return performArithmetic(request, "ADD");
    }

    // ==================== SUBTRACT ====================
    public Map<String, Object> subtract(QuantityArithmeticRequest request) {
        // Temperature does not support arithmetic operations
        if ("TEMPERATURE".equals(request.getType())) {
            throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
        }
        return performArithmetic(request, "SUBTRACT");
    }

    // ==================== MULTIPLY ====================
    public Map<String, Object> multiply(QuantityArithmeticRequest request) {
        // Temperature does not support arithmetic operations
        if ("TEMPERATURE".equals(request.getType())) {
            throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
        }
        return performArithmetic(request, "MULTIPLY");
    }

    // ==================== DIVIDE ====================
    public Map<String, Object> divide(QuantityArithmeticRequest request) {
        // Temperature does not support arithmetic operations
        if ("TEMPERATURE".equals(request.getType())) {
            throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
        }
        return performArithmetic(request, "DIVIDE");
    }

    // ==================== ARITHMETIC HELPER ====================
    private Map<String, Object> performArithmetic(QuantityArithmeticRequest request, String operation) {
        UserEntity user = getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        
        try {
            double value1InBase = convertToBase(request.getValue1(), request.getUnit1(), request.getType());
            double value2InBase = convertToBase(request.getValue2(), request.getUnit2(), request.getType());
            
            double resultInBase;
            switch (operation) {
                case "ADD": resultInBase = value1InBase + value2InBase; break;
                case "SUBTRACT": resultInBase = value1InBase - value2InBase; break;
                case "MULTIPLY": resultInBase = value1InBase * value2InBase; break;
                case "DIVIDE": 
                    if (value2InBase == 0) throw new ArithmeticException("Division by zero");
                    resultInBase = value1InBase / value2InBase;
                    break;
                default: resultInBase = 0;
            }
            
            double finalValue = convertFromBase(resultInBase, request.getUnit1(), request.getType());
            
            String symbol = operation.equals("ADD") ? "+" : 
                           operation.equals("SUBTRACT") ? "-" : 
                           operation.equals("MULTIPLY") ? "×" : "÷";
            
            String resultText = String.format("%.4f %s %s %.4f %s = %.4f %s", 
                request.getValue1(), request.getUnit1(), symbol,
                request.getValue2(), request.getUnit2(), finalValue, request.getUnit1());
            
            result.put("success", true);
            result.put("value", finalValue);
            result.put("unit", request.getUnit1());
            result.put("result", resultText);
            
            // Save to history ONLY if user is logged in
            if (user != null) {
                historyService.saveHistory(user, operation, request.getType(),
                    request.getValue1(), request.getUnit1(), 
                    request.getValue2(), request.getUnit2(),
                    finalValue, request.getUnit1(), resultText, false, null);
            }
                
        } catch (Exception e) {
            if (user != null) {
                historyService.saveHistory(user, operation, request.getType(),
                    request.getValue1(), request.getUnit1(), 
                    request.getValue2(), request.getUnit2(),
                    null, null, null, true, e.getMessage());
            }
            throw e;
        }
        return result;
    }

    // ==================== CONVERSION HELPERS ====================
    private double convertToBase(double value, String unit, String type) {
        Map<String, Double> units = conversionFactors.get(type);
        if (units == null || !units.containsKey(unit.toUpperCase())) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        return value * units.get(unit.toUpperCase());
    }

    private double convertFromBase(double baseValue, String unit, String type) {
        Map<String, Double> units = conversionFactors.get(type);
        if (units == null || !units.containsKey(unit.toUpperCase())) {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
        return baseValue / units.get(unit.toUpperCase());
    }

    // ✅ Temperature conversion methods (independent of conversionFactors map)
    private double convertTemperatureToBase(double value, String unit) {
        String upperUnit = unit.toUpperCase();
        switch (upperUnit) {
            case "CELSIUS":
                return value;
            case "FAHRENHEIT":
                return (value - 32) * 5 / 9;
            case "KELVIN":
                return value - 273.15;
            default:
                throw new IllegalArgumentException("Invalid temperature unit: " + unit + ". Supported units: CELSIUS, FAHRENHEIT, KELVIN");
        }
    }

    private double convertTemperatureFromBase(double celsius, String unit) {
        String upperUnit = unit.toUpperCase();
        switch (upperUnit) {
            case "CELSIUS":
                return celsius;
            case "FAHRENHEIT":
                return celsius * 9 / 5 + 32;
            case "KELVIN":
                return celsius + 273.15;
            default:
                throw new IllegalArgumentException("Invalid temperature unit: " + unit + ". Supported units: CELSIUS, FAHRENHEIT, KELVIN");
        }
    }

    // ==================== GET ALL UNITS ====================
    public Map<String, Object> getAllUnits() {
        Map<String, Object> allUnits = new HashMap<>();
        
        Map<String, Object> length = new HashMap<>();
        length.put("units", new String[]{"METER", "KILOMETER", "CENTIMETER", "MILLIMETER", "MILE", "YARD", "FOOT", "INCH"});
        length.put("base", "METER");
        
        Map<String, Object> weight = new HashMap<>();
        weight.put("units", new String[]{"KILOGRAM", "GRAM", "MILLIGRAM", "POUND", "OUNCE"});
        weight.put("base", "KILOGRAM");
        
        Map<String, Object> volume = new HashMap<>();
        volume.put("units", new String[]{"LITER", "MILLILITER", "GALLON", "QUART", "PINT"});
        volume.put("base", "LITER");
        
        Map<String, Object> temperature = new HashMap<>();
        temperature.put("units", new String[]{"CELSIUS", "FAHRENHEIT", "KELVIN"});
        temperature.put("base", "CELSIUS");
        
        allUnits.put("LENGTH", length);
        allUnits.put("WEIGHT", weight);
        allUnits.put("VOLUME", volume);
        allUnits.put("TEMPERATURE", temperature);
        
        return allUnits;
    }
}