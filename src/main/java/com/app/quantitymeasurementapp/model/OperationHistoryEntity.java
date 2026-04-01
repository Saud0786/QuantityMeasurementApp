package com.app.quantitymeasurementapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "measurement_type", nullable = false)
    private String measurementType;

    @Column(name = "value1", nullable = false)
    private Double value1;

    @Column(name = "unit1", nullable = false)
    private String unit1;

    @Column(name = "value2")
    private Double value2;

    @Column(name = "unit2")
    private String unit2;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit")
    private String resultUnit;

    @Column(name = "result_text", length = 500)
    private String resultText;

    @Column(name = "is_error")
    private boolean isError;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}