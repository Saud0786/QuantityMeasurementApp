package com.app.history.repository;

import com.app.history.model.OperationHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistoryEntity, Long> {

    List<OperationHistoryEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<OperationHistoryEntity> findByUserIdAndOperationTypeOrderByCreatedAtDesc(Long userId, String operationType);
    
    List<OperationHistoryEntity> findByUserIdAndMeasurementTypeOrderByCreatedAtDesc(Long userId, String measurementType);
    
    List<OperationHistoryEntity> findByUserIdAndIsErrorTrueOrderByCreatedAtDesc(Long userId);
    
    List<OperationHistoryEntity> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, LocalDateTime date);
    
    @Query("SELECT COUNT(h) FROM OperationHistoryEntity h WHERE h.userId = :userId AND h.operationType = :operationType AND h.isError = false")
    long countSuccessfulOperations(@Param("userId") Long userId, @Param("operationType") String operationType);
    
    @Query("DELETE FROM OperationHistoryEntity h WHERE h.userId = :userId AND h.createdAt < :date")
    void deleteOldHistory(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    @jakarta.transaction.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @Query("DELETE FROM OperationHistoryEntity h WHERE h.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}