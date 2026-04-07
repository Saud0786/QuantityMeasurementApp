package com.app.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponseDTO {
    private Long id;
    private String operationType;
    private String measurementType;
    private String input;
    private String result;
    private boolean isError;
    private String errorMessage;
    private LocalDateTime createdAt;
}