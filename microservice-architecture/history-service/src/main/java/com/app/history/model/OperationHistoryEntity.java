package com.app.history.model;

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

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "measurement_type")
    private String measurementType;

    @Column(name = "value1")
    private Double value1;

    @Column(name = "unit1")
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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}