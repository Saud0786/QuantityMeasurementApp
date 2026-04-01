package com.app.quantitymeasurementapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.app.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.app.quantitymeasurementapp.dto.QuantityArithmeticRequest;
import com.app.quantitymeasurementapp.dto.QuantityCompareRequest;
import com.app.quantitymeasurementapp.dto.QuantityConvertRequest;

@SpringBootTest
class QuantityMeasurementAppApplicationTests {
	
	@Autowired
	private QuantityMeasurementController controller;
	
	// ==================== COMPARISON TESTS ====================
	
	@Test
	void testCompare_Length_SameUnit_Equal() {
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(5.0);
		request.setUnit1("METER");
		request.setValue2(5.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Length_SameUnit_Greater() {
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(10.0);
		request.setUnit1("METER");
		request.setValue2(5.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("greater than", result.get("comparison"));
	}
	
	@Test
	void testCompare_Length_SameUnit_Less() {
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(3.0);
		request.setUnit1("METER");
		request.setValue2(10.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("less than", result.get("comparison"));
	}
	
	@Test
	void testCompare_Length_DifferentUnits_Equal() {
		// 1 meter = 100 centimeters
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(1.0);
		request.setUnit1("METER");
		request.setValue2(100.0);
		request.setUnit2("CENTIMETER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Length_DifferentUnits_Greater() {
		// 2 meters > 100 centimeters
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(2.0);
		request.setUnit1("METER");
		request.setValue2(100.0);
		request.setUnit2("CENTIMETER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("greater than", result.get("comparison"));
	}
	
	@Test
	void testCompare_Weight_SameUnit_Equal() {
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(1.0);
		request.setUnit1("KILOGRAM");
		request.setValue2(1.0);
		request.setUnit2("KILOGRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Weight_DifferentUnits_Equal() {
		// 1 kilogram = 1000 grams
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(1.0);
		request.setUnit1("KILOGRAM");
		request.setValue2(1000.0);
		request.setUnit2("GRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Volume_DifferentUnits_Equal() {
		// 1 liter = 1000 milliliters
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(1.0);
		request.setUnit1("LITER");
		request.setValue2(1000.0);
		request.setUnit2("MILLILITER");
		request.setType("VOLUME");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Temperature_CelsiusToFahrenheit_Equal() {
		// 0°C = 32°F
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(0.0);
		request.setUnit1("CELSIUS");
		request.setValue2(32.0);
		request.setUnit2("FAHRENHEIT");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Temperature_CelsiusToKelvin_Equal() {
		// 0°C = 273.15K
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(0.0);
		request.setUnit1("CELSIUS");
		request.setValue2(273.15);
		request.setUnit2("KELVIN");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Temperature_FahrenheitToKelvin_Equal() {
		// 32°F = 273.15K
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(32.0);
		request.setUnit1("FAHRENHEIT");
		request.setValue2(273.15);
		request.setUnit2("KELVIN");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testCompare_Temperature_CelsiusGreaterThanFahrenheit() {
		// 100°C > 100°F
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(100.0);
		request.setUnit1("CELSIUS");
		request.setValue2(100.0);
		request.setUnit2("FAHRENHEIT");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("greater than", result.get("comparison"));
	}
	
	// ==================== CONVERSION TESTS ====================
	
	@Test
	void testConvert_Length_MeterToCentimeter() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("METER");
		request.setToUnit("CENTIMETER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(100.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Length_KilometerToMeter() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("KILOMETER");
		request.setToUnit("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1000.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Length_MileToKilometer() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("MILE");
		request.setToUnit("KILOMETER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1.609344, (Double) result.get("value"), 0.0001);
	}
	
	@Test
	void testConvert_Weight_KilogramToGram() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("KILOGRAM");
		request.setToUnit("GRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1000.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Weight_PoundToKilogram() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("POUND");
		request.setToUnit("KILOGRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(0.453592, (Double) result.get("value"), 0.0001);
	}
	
	@Test
	void testConvert_Volume_LiterToMilliliter() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("LITER");
		request.setToUnit("MILLILITER");
		request.setType("VOLUME");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1000.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Volume_GallonToLiter() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("GALLON");
		request.setToUnit("LITER");
		request.setType("VOLUME");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(3.78541, (Double) result.get("value"), 0.0001);
	}
	
	@Test
	void testConvert_Temperature_CelsiusToFahrenheit() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(0.0);
		request.setFromUnit("CELSIUS");
		request.setToUnit("FAHRENHEIT");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(32.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Temperature_FahrenheitToCelsius() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(32.0);
		request.setFromUnit("FAHRENHEIT");
		request.setToUnit("CELSIUS");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(0.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Temperature_CelsiusToKelvin() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(0.0);
		request.setFromUnit("CELSIUS");
		request.setToUnit("KELVIN");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(273.15, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Temperature_KelvinToCelsius() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(273.15);
		request.setFromUnit("KELVIN");
		request.setToUnit("CELSIUS");
		request.setType("TEMPERATURE");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(0.0, (Double) result.get("value"), 0.001);
	}
	
	// ==================== ARITHMETIC TESTS ====================
	
	@Test
	void testAdd_Length_SameUnit() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(5.0);
		request.setUnit1("METER");
		request.setValue2(3.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.add(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(8.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testAdd_Length_DifferentUnits() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(1.0);
		request.setUnit1("METER");
		request.setValue2(100.0);
		request.setUnit2("CENTIMETER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.add(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(2.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testAdd_Weight_KilogramAndGram() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(1.0);
		request.setUnit1("KILOGRAM");
		request.setValue2(500.0);
		request.setUnit2("GRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.add(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1.5, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testAdd_Volume_LiterAndMilliliter() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(1.0);
		request.setUnit1("LITER");
		request.setValue2(500.0);
		request.setUnit2("MILLILITER");
		request.setType("VOLUME");
		
		ResponseEntity<Map<String, Object>> response = controller.add(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1.5, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testSubtract_Length() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(10.0);
		request.setUnit1("METER");
		request.setValue2(300.0);
		request.setUnit2("CENTIMETER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.subtract(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(7.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testSubtract_Weight() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(2.0);
		request.setUnit1("KILOGRAM");
		request.setValue2(500.0);
		request.setUnit2("GRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.subtract(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(1.5, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testMultiply_Length() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(5.0);
		request.setUnit1("METER");
		request.setValue2(3.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.multiply(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(15.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testDivide_Length() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(10.0);
		request.setUnit1("METER");
		request.setValue2(2.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.divide(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(5.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testDivide_ByZero_ThrowsException() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(10.0);
		request.setUnit1("METER");
		request.setValue2(0.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		assertThrows(Exception.class, () -> {
			controller.divide(request);
		});
	}
	
	@Test
	void testTemperature_Arithmetic_NotSupported() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(10.0);
		request.setUnit1("CELSIUS");
		request.setValue2(5.0);
		request.setUnit2("CELSIUS");
		request.setType("TEMPERATURE");
		
		assertThrows(IllegalArgumentException.class, () -> {
			controller.add(request);
		});
	}
	
	@Test
	void testTemperature_Subtract_NotSupported() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(10.0);
		request.setUnit1("CELSIUS");
		request.setValue2(5.0);
		request.setUnit2("CELSIUS");
		request.setType("TEMPERATURE");
		
		assertThrows(IllegalArgumentException.class, () -> {
			controller.subtract(request);
		});
	}
	
	// ==================== GET UNITS TEST ====================
	
	@Test
	void testGetAllUnits() {
		ResponseEntity<Map<String, Object>> response = controller.getUnits();
		Map<String, Object> units = response.getBody();
		
		assertNotNull(units);
		assertTrue(units.containsKey("LENGTH"));
		assertTrue(units.containsKey("WEIGHT"));
		assertTrue(units.containsKey("VOLUME"));
		assertTrue(units.containsKey("TEMPERATURE"));
		
		// Verify each category has units
		Map<String, Object> length = (Map<String, Object>) units.get("LENGTH");
		assertNotNull(length.get("units"));
		assertNotNull(length.get("base"));
		
		Map<String, Object> weight = (Map<String, Object>) units.get("WEIGHT");
		assertNotNull(weight.get("units"));
		assertNotNull(weight.get("base"));
		
		Map<String, Object> volume = (Map<String, Object>) units.get("VOLUME");
		assertNotNull(volume.get("units"));
		assertNotNull(volume.get("base"));
		
		Map<String, Object> temperature = (Map<String, Object>) units.get("TEMPERATURE");
		assertNotNull(temperature.get("units"));
		assertNotNull(temperature.get("base"));
	}
	
	// ==================== ERROR HANDLING TESTS ====================
	
	@Test
	void testCompare_InvalidUnit_ThrowsException() {
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(10.0);
		request.setUnit1("INVALID_UNIT");
		request.setValue2(5.0);
		request.setUnit2("METER");
		request.setType("LENGTH");
		
		assertThrows(Exception.class, () -> {
			controller.compare(request);
		});
	}
	
	@Test
	void testConvert_InvalidFromUnit_ThrowsException() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(10.0);
		request.setFromUnit("INVALID_UNIT");
		request.setToUnit("METER");
		request.setType("LENGTH");
		
		assertThrows(Exception.class, () -> {
			controller.convert(request);
		});
	}
	
	@Test
	void testConvert_InvalidToUnit_ThrowsException() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(10.0);
		request.setFromUnit("METER");
		request.setToUnit("INVALID_UNIT");
		request.setType("LENGTH");
		
		assertThrows(Exception.class, () -> {
			controller.convert(request);
		});
	}
	
	@Test
	void testAdd_CrossCategory_ThrowsException() {
		QuantityArithmeticRequest request = new QuantityArithmeticRequest();
		request.setValue1(10.0);
		request.setUnit1("METER");
		request.setValue2(5.0);
		request.setUnit2("KILOGRAM");
		request.setType("LENGTH");
		
		assertThrows(Exception.class, () -> {
			controller.add(request);
		});
	}
	
	// ==================== ADDITIONAL UNIT TESTS ====================
	
	@Test
	void testCompare_Length_FootAndInch() {
		// 1 foot = 12 inches
		QuantityCompareRequest request = new QuantityCompareRequest();
		request.setValue1(1.0);
		request.setUnit1("FOOT");
		request.setValue2(12.0);
		request.setUnit2("INCH");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.compare(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals("equal to", result.get("comparison"));
	}
	
	@Test
	void testConvert_Length_YardToFoot() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("YARD");
		request.setToUnit("FOOT");
		request.setType("LENGTH");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(3.0, (Double) result.get("value"), 0.001);
	}
	
	@Test
	void testConvert_Weight_OunceToGram() {
		QuantityConvertRequest request = new QuantityConvertRequest();
		request.setValue(1.0);
		request.setFromUnit("OUNCE");
		request.setToUnit("GRAM");
		request.setType("WEIGHT");
		
		ResponseEntity<Map<String, Object>> response = controller.convert(request);
		Map<String, Object> result = response.getBody();
		
		assertTrue((Boolean) result.get("success"));
		assertEquals(28.3495, (Double) result.get("value"), 0.0001);
	}
}