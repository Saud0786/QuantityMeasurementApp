package com.app.quantity.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveHistoryRequest {
    private String operationType;
    private String measurementType;
    private Double value1;
    private String unit1;
    private Double value2;
    private String unit2;
    private Double resultValue;
    private String resultUnit;
    private String resultText;
    private boolean isError;
    private String errorMessage;
}