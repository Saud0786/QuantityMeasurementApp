package com.apps.quantitymeasurement;

@FunctionalInterface
interface SupportsArithmetic {
    boolean isSupported();
}

public interface IMeasurable {

    // 🔴 Existing methods – DO NOT REMOVE (backward compatibility)
    double getConversionFactor();

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    String getUnitName();

    // 🟢 UC14 – New default capability methods (non-breaking)
    default SupportsArithmetic supportsArithmetic() {
        return () -> true; // Length, Weight, Volume inherit this
    }

    default void validateOperationSupport(String operation) {
        // By default, all operations are supported
    }
}