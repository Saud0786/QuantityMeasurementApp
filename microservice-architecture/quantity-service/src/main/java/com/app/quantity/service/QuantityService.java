package com.app.quantity.service;

import com.app.quantity.client.HistoryServiceClient;
import com.app.quantity.client.SaveHistoryRequest;
import com.app.quantity.dto.QuantityArithmeticRequest;
import com.app.quantity.dto.QuantityCompareRequest;
import com.app.quantity.dto.QuantityConvertRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuantityService {

	private final HistoryServiceClient historyServiceClient;
	private final HttpServletRequest request;

private Long getCurrentUserId() {
    // Get from header (sent by Auth Service via wrapped request)
    String userIdHeader = request.getHeader("X-User-Id");
    System.out.println("🔍 X-User-Id header in Quantity Service: " + userIdHeader);
    
    if (userIdHeader != null && !userIdHeader.isEmpty()) {
        try {
            Long id = Long.parseLong(userIdHeader);
            if (id != null && id > 0) {
                return id;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid X-User-Id format: " + userIdHeader);
        }
    }
    
    // ✅ FIX: Don't return default 1, return null instead
    System.out.println("⚠️ No valid X-User-Id found");
    return null;  // Return null instead of default 1
}

private String getCurrentUserEmail() {
    // Get from header
    String userEmail = request.getHeader("X-User-Email");
    System.out.println("🔍 X-User-Email header in Quantity Service: " + userEmail);
    
    if (userEmail != null && !userEmail.isEmpty()) {
        return userEmail;
    }
    
    // Fallback: try attribute
    Object emailAttr = request.getAttribute("X-User-Email");
    if (emailAttr != null) {
        System.out.println("🔍 X-User-Email attribute: " + emailAttr);
        return emailAttr.toString();
    }
    
    System.out.println("⚠️ No X-User-Email found");
    return null;  // Return null instead of default
}
private void saveHistory(String operationType, String measurementType,
                        Double value1, String unit1, Double value2, String unit2,
                        Double resultValue, String resultUnit, String resultText,
                        boolean isError, String errorMessage) {
    
    Long userId = getCurrentUserId();
    String userEmail = getCurrentUserEmail();
    
    System.out.println("🔍 SAVING HISTORY - UserId: " + userId + ", Email: " + userEmail);
    
    // ✅ FIX: Agar userId null hai to history save mat karo
    if (userId == null || userId == 1L) {  // 1L is default, skip it
        System.out.println("⚠️ Cannot save history - User not authenticated (userId: " + userId + ")");
        return;
    }
    
    if (userEmail == null || "default@example.com".equals(userEmail)) {
        System.out.println("⚠️ Cannot save history - Invalid user email: " + userEmail);
        return;
    }
    
    SaveHistoryRequest historyRequest = SaveHistoryRequest.builder()
            .operationType(operationType)
            .measurementType(measurementType)
            .value1(value1)
            .unit1(unit1)
            .value2(value2)
            .unit2(unit2)
            .resultValue(resultValue)
            .resultUnit(resultUnit)
            .resultText(resultText)
            .isError(isError)
            .errorMessage(errorMessage)
            .build();
    
    try {
        // ✅ Pass headers explicitly
        historyServiceClient.saveHistory(historyRequest, userId, userEmail);
        System.out.println("✅ History saved for user: " + userEmail + " (ID: " + userId + ")");
    } catch (Exception e) {
        System.err.println("❌ Failed to save history: " + e.getMessage());
        e.printStackTrace();
    }
}
	// Unit conversion factors
	private static final Map<String, Map<String, Double>> conversionFactors = new HashMap<>();

	static {
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

		Map<String, Double> weightUnits = new HashMap<>();
		weightUnits.put("KILOGRAM", 1.0);
		weightUnits.put("GRAM", 0.001);
		weightUnits.put("MILLIGRAM", 0.000001);
		weightUnits.put("POUND", 0.453592);
		weightUnits.put("OUNCE", 0.0283495);
		conversionFactors.put("WEIGHT", weightUnits);

		Map<String, Double> volumeUnits = new HashMap<>();
		volumeUnits.put("LITER", 1.0);
		volumeUnits.put("MILLILITER", 0.001);
		volumeUnits.put("GALLON", 3.78541);
		volumeUnits.put("QUART", 0.946353);
		volumeUnits.put("PINT", 0.473176);
		conversionFactors.put("VOLUME", volumeUnits);
	}

	public Map<String, Object> compare(QuantityCompareRequest request) {
		Map<String, Object> result = new HashMap<>();

		try {
			double value1InBase;
			double value2InBase;

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

			String resultText = String.format("%.4f %s is %s %.4f %s", request.getValue1(), request.getUnit1(),
					comparison, request.getValue2(), request.getUnit2());

			result.put("success", true);
			result.put("comparison", comparison);
			result.put("message", resultText);

			saveHistory("COMPARE", request.getType(), request.getValue1(), request.getUnit1(), request.getValue2(),
					request.getUnit2(), null, null, resultText, false, null);

		} catch (Exception e) {
			saveHistory("COMPARE", request.getType(), request.getValue1(), request.getUnit1(), request.getValue2(),
					request.getUnit2(), null, null, null, true, e.getMessage());
			throw e;
		}
		return result;
	}

	public Map<String, Object> convert(QuantityConvertRequest request) {
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

			String resultText = String.format("%.4f %s = %.4f %s", request.getValue(), request.getFromUnit(),
					convertedValue, request.getToUnit());

			result.put("success", true);
			result.put("value", convertedValue);
			result.put("result", resultText);

			saveHistory("CONVERT", request.getType(), request.getValue(), request.getFromUnit(), null, null,
					convertedValue, request.getToUnit(), resultText, false, null);

		} catch (Exception e) {
			saveHistory("CONVERT", request.getType(), request.getValue(), request.getFromUnit(), null, null, null, null,
					null, true, e.getMessage());
			throw e;
		}
		return result;
	}

	public Map<String, Object> add(QuantityArithmeticRequest request) {
		if ("TEMPERATURE".equals(request.getType())) {
			throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
		}
		return performArithmetic(request, "ADD");
	}

	public Map<String, Object> subtract(QuantityArithmeticRequest request) {
		if ("TEMPERATURE".equals(request.getType())) {
			throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
		}
		return performArithmetic(request, "SUBTRACT");
	}

	public Map<String, Object> multiply(QuantityArithmeticRequest request) {
		if ("TEMPERATURE".equals(request.getType())) {
			throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
		}
		return performArithmetic(request, "MULTIPLY");
	}

	public Map<String, Object> divide(QuantityArithmeticRequest request) {
		if ("TEMPERATURE".equals(request.getType())) {
			throw new IllegalArgumentException("Arithmetic operations are not supported for Temperature measurements");
		}
		return performArithmetic(request, "DIVIDE");
	}

	private Map<String, Object> performArithmetic(QuantityArithmeticRequest request, String operation) {
		Map<String, Object> result = new HashMap<>();

		try {
			double value1InBase = convertToBase(request.getValue1(), request.getUnit1(), request.getType());
			double value2InBase = convertToBase(request.getValue2(), request.getUnit2(), request.getType());

			double resultInBase;
			switch (operation) {
			case "ADD":
				resultInBase = value1InBase + value2InBase;
				break;
			case "SUBTRACT":
				resultInBase = value1InBase - value2InBase;
				break;
			case "MULTIPLY":
				resultInBase = value1InBase * value2InBase;
				break;
			case "DIVIDE":
				if (value2InBase == 0)
					throw new ArithmeticException("Division by zero");
				resultInBase = value1InBase / value2InBase;
				break;
			default:
				resultInBase = 0;
			}

			double finalValue = convertFromBase(resultInBase, request.getUnit1(), request.getType());

			String symbol = operation.equals("ADD") ? "+"
					: operation.equals("SUBTRACT") ? "-" : operation.equals("MULTIPLY") ? "×" : "÷";

			String resultText = String.format("%.4f %s %s %.4f %s = %.4f %s", request.getValue1(), request.getUnit1(),
					symbol, request.getValue2(), request.getUnit2(), finalValue, request.getUnit1());

			result.put("success", true);
			result.put("value", finalValue);
			result.put("unit", request.getUnit1());
			result.put("result", resultText);

			saveHistory(operation, request.getType(), request.getValue1(), request.getUnit1(), request.getValue2(),
					request.getUnit2(), finalValue, request.getUnit1(), resultText, false, null);

		} catch (Exception e) {
			saveHistory(operation, request.getType(), request.getValue1(), request.getUnit1(), request.getValue2(),
					request.getUnit2(), null, null, null, true, e.getMessage());
			throw e;
		}
		return result;
	}

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
			throw new IllegalArgumentException("Invalid temperature unit: " + unit);
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
			throw new IllegalArgumentException("Invalid temperature unit: " + unit);
		}
	}

	public Map<String, Object> getAllUnits() {
		Map<String, Object> allUnits = new HashMap<>();

		Map<String, Object> length = new HashMap<>();
		length.put("units",
				new String[] { "METER", "KILOMETER", "CENTIMETER", "MILLIMETER", "MILE", "YARD", "FOOT", "INCH" });
		length.put("base", "METER");

		Map<String, Object> weight = new HashMap<>();
		weight.put("units", new String[] { "KILOGRAM", "GRAM", "MILLIGRAM", "POUND", "OUNCE" });
		weight.put("base", "KILOGRAM");

		Map<String, Object> volume = new HashMap<>();
		volume.put("units", new String[] { "LITER", "MILLILITER", "GALLON", "QUART", "PINT" });
		volume.put("base", "LITER");

		Map<String, Object> temperature = new HashMap<>();
		temperature.put("units", new String[] { "CELSIUS", "FAHRENHEIT", "KELVIN" });
		temperature.put("base", "CELSIUS");

		allUnits.put("LENGTH", length);
		allUnits.put("WEIGHT", weight);
		allUnits.put("VOLUME", volume);
		allUnits.put("TEMPERATURE", temperature);

		return allUnits;
	}
}