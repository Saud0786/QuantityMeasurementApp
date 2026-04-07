package com.app.quantity.dto;

import lombok.Data;

@Data
public class QuantityConvertRequest {
    private double value;
    private String fromUnit;
    private String toUnit;
    private String type;
}