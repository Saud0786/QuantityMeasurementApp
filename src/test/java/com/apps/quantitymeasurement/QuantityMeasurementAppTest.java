package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.apps.quantitymeasurement.Length.LengthUnit;

public class QuantityMeasurementAppTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(1.0, LengthUnit.FEET);
        assertTrue(l1.equals(l2));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0, LengthUnit.INCHES);
        assertTrue(l1.equals(l2));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        Length inches = new Length(12.0, LengthUnit.INCHES);
        Length feet = new Length(1.0, LengthUnit.FEET);
        assertTrue(inches.equals(feet));
        assertTrue(feet.equals(inches)); // symmetry
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(2.0, LengthUnit.FEET);
        assertFalse(l1.equals(l2));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.INCHES);
        Length l2 = new Length(2.0, LengthUnit.INCHES);
        assertFalse(l1.equals(l2));
    }

    @Test
    void testEquality_NullComparison() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        assertFalse(l1.equals(null));
    }

    @Test
    void testEquality_SameReference() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        assertTrue(l1.equals(l1));
    }

    @Test
    void testInvalidUnit_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Length(1.0, null);
        });
    }
    
    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Length(5.0, null);
        });
    }
    
    
    @Test
    void testEquality_InvalidUnit() {
        // Simulate invalid unit by passing null or unsupported scenario
        assertThrows(IllegalArgumentException.class, () -> {
            new Length(1.0, null);  // invalid unit
        });
    }
}