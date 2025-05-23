package com.socompany.orderservice.repository;

import com.socompany.orderservice.persistant.entity.RetryableTask;
import com.socompany.orderservice.persistant.enums.RetryableTaskStatus;
import com.socompany.orderservice.persistant.enums.RetryableTaskType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface RetryableTaskRepository extends JpaRepository<RetryableTask, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r from  RetryableTask r where r.type = :type and r.retryTime <= :retryTime " +
            "and r.status = :status " +
            "order by r.retryTime asc")
    public List<RetryableTask> findRetryableTasksForProcessing(RetryableTaskType type,
                                                               RetryableTaskStatus status,
                                                               Pageable pageable,
                                                               Instant retryTime);
}
