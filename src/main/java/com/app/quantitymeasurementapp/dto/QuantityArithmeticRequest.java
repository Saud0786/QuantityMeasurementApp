package com.app.quantitymeasurementapp.dto;

import lombok.Data;

@Data
public class QuantityArithmeticRequest {
    private double value1;
    private String unit1;
    private double value2;
    private String unit2;
    private String type;
}