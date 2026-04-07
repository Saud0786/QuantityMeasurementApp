package com.app.quantitymeasurementapp.repository;

import com.app.quantitymeasurementapp.model.OperationHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistoryEntity, Long> {

    // Get user's operation history
    List<OperationHistoryEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // Get by operation type
    List<OperationHistoryEntity> findByUserIdAndOperationTypeOrderByCreatedAtDesc(Long userId, String operationType);
    
    // Get by measurement type
    List<OperationHistoryEntity> findByUserIdAndMeasurementTypeOrderByCreatedAtDesc(Long userId, String measurementType);
    
    // Get error history
    List<OperationHistoryEntity> findByUserIdAndIsErrorTrueOrderByCreatedAtDesc(Long userId);
    
    // Get recent history (last 7 days)
    List<OperationHistoryEntity> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, LocalDateTime date);
    
    // Count operations by type
    @Query("SELECT COUNT(h) FROM OperationHistoryEntity h WHERE h.userId = :userId AND h.operationType = :operationType AND h.isError = false")
    long countSuccessfulOperations(@Param("userId") Long userId, @Param("operationType") String operationType);
    
    // Delete old history (older than days)
    @Query("DELETE FROM OperationHistoryEntity h WHERE h.userId = :userId AND h.createdAt < :date")
    void deleteOldHistory(@Param("userId") Long userId, @Param("date") LocalDateTime date);
    
    // Delete all user history
    void deleteAllByUserId(Long userId);
}