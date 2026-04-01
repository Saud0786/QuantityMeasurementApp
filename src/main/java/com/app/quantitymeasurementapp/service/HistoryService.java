package com.app.quantitymeasurementapp.service;

import com.app.quantitymeasurementapp.dto.HistoryResponseDTO;
import com.app.quantitymeasurementapp.model.OperationHistoryEntity;
import com.app.quantitymeasurementapp.model.UserEntity;
import com.app.quantitymeasurementapp.repository.OperationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final OperationHistoryRepository historyRepository;

    public void saveHistory(UserEntity user, String operationType, String measurementType,
                           Double value1, String unit1, Double value2, String unit2,
                           Double resultValue, String resultUnit, String resultText,
                           boolean isError, String errorMessage) {
        
        OperationHistoryEntity history = OperationHistoryEntity.builder()
                .userId(user.getId())
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
        
        historyRepository.save(history);
    }

    public List<HistoryResponseDTO> getUserHistory(UserEntity user, int limit) {
        return historyRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HistoryResponseDTO> getUserHistoryByOperation(UserEntity user, String operationType) {
        return historyRepository.findByUserIdAndOperationTypeOrderByCreatedAtDesc(user.getId(), operationType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HistoryResponseDTO> getUserHistoryByMeasurementType(UserEntity user, String measurementType) {
        return historyRepository.findByUserIdAndMeasurementTypeOrderByCreatedAtDesc(user.getId(), measurementType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HistoryResponseDTO> getUserErrorHistory(UserEntity user) {
        return historyRepository.findByUserIdAndIsErrorTrueOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getOperationCount(UserEntity user, String operationType) {
        return historyRepository.countSuccessfulOperations(user.getId(), operationType);
    }

    private HistoryResponseDTO convertToDTO(OperationHistoryEntity entity) {
        String input;
        if (entity.getValue2() != null) {
            input = String.format("%.4f %s %s %.4f %s", 
                entity.getValue1(), entity.getUnit1(),
                getOperationSymbol(entity.getOperationType()),
                entity.getValue2(), entity.getUnit2());
        } else {
            input = String.format("%.4f %s", entity.getValue1(), entity.getUnit1());
        }
        
        return HistoryResponseDTO.builder()
                .id(entity.getId())
                .operationType(entity.getOperationType())
                .measurementType(entity.getMeasurementType())
                .input(input)
                .result(entity.isError() ? "Error: " + entity.getErrorMessage() : entity.getResultText())
                .isError(entity.isError())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private String getOperationSymbol(String operation) {
        switch (operation.toUpperCase()) {
            case "COMPARE": return "vs";
            case "CONVERT": return "→";
            case "ADD": return "+";
            case "SUBTRACT": return "-";
            case "MULTIPLY": return "×";
            case "DIVIDE": return "÷";
            default: return operation;
        }
    }
}