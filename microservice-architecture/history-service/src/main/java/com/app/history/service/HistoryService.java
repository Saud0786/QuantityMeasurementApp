package com.app.history.service;

import com.app.history.dto.HistoryResponseDTO;
import com.app.history.dto.SaveHistoryRequest;
import com.app.history.model.OperationHistoryEntity;
import com.app.history.repository.OperationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final OperationHistoryRepository historyRepository;

    public void saveHistory(SaveHistoryRequest request, Long userId, String userEmail) {
        OperationHistoryEntity history = OperationHistoryEntity.builder()
                .userId(userId)
                .userEmail(userEmail)
                .operationType(request.getOperationType())
                .measurementType(request.getMeasurementType())
                .value1(request.getValue1())
                .unit1(request.getUnit1())
                .value2(request.getValue2())
                .unit2(request.getUnit2())
                .resultValue(request.getResultValue())
                .resultUnit(request.getResultUnit())
                .resultText(request.getResultText())
                .isError(request.isError())
                .errorMessage(request.getErrorMessage())
                .build();
        
        historyRepository.save(history);
        System.out.println("✅ History saved: " + request.getOperationType() + " | User: " + userEmail);
    }

    public List<HistoryResponseDTO> getUserHistory(Long userId, int limit) {
        return historyRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HistoryResponseDTO> getUserHistoryByOperation(Long userId, String operationType) {
        return historyRepository.findByUserIdAndOperationTypeOrderByCreatedAtDesc(userId, operationType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ ADD THIS MISSING METHOD
    public List<HistoryResponseDTO> getUserHistoryByMeasurementType(Long userId, String measurementType) {
        return historyRepository.findByUserIdAndMeasurementTypeOrderByCreatedAtDesc(userId, measurementType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HistoryResponseDTO> getUserErrorHistory(Long userId) {
        return historyRepository.findByUserIdAndIsErrorTrueOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ ADD THIS MISSING METHOD
    public long getOperationCount(Long userId, String operationType) {
        return historyRepository.countSuccessfulOperations(userId, operationType);
    }

    // ✅ CLEAR HISTORY METHOD
    public void clearUserHistory(Long userId) {
        historyRepository.deleteByUserId(userId);
        System.out.println("✅ Cleared history for User ID: " + userId);
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