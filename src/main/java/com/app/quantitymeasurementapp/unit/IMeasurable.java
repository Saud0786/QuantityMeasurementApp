package com.app.quantitymeasurementapp.unit;



@FunctionalInterface
interface SupportsArithmetic{
    boolean isSupported();
}



public interface IMeasurable {
     double getConversionFactor();
     
     double convertToBaseUnit(double value);
     
     double convertFromBaseUnit(double value);
     
     String getUnitName();
     
     String getMeasurementType();
     
     IMeasurable getUnitInstance(String unitName);
     
     SupportsArithmetic supportArithemetic = ()-> true;
     
     default boolean supportsArithemetics() {
    	 return supportArithemetic.isSupported();
     }
     
     default void validateOperationSupport(String operation){
     }
}