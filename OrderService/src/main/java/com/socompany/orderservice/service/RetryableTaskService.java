package com.socompany.orderservice.service;

import com.socompany.orderservice.persistant.entity.Order;
import com.socompany.orderservice.persistant.entity.RetryableTask;
import com.socompany.orderservice.persistant.enums.RetryableTaskStatus;
import com.socompany.orderservice.persistant.enums.RetryableTaskType;
import com.socompany.orderservice.repository.RetryableTaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RetryableTaskService {
    private final RetryableTaskRepository retryableTaskRepository;


    @Value("${retryabletask.timeoutInSeconds}")
    private Integer timeoutInSeconds;

    @Value("${retryabletask.limit}")
    private Integer limit;

    @Transactional
    public RetryableTask createRetryableTask(Order order, RetryableTaskType retryableTaskType) {
        log.info("Creating retryable task for order: {}", order.getId());
        RetryableTask retryableTask = new RetryableTask(order,
                retryableTaskType,
                RetryableTaskStatus.IN_PROGRESS,
                Instant.now());

        retryableTaskRepository.save(retryableTask);
        		return retryableTask;
    }

    @Transactional
    public List<RetryableTask> getRetryableTasksForProcessing(RetryableTaskType type) {
        log.info("Getting retryable tasks for processing");
        var currTime = Instant.now();
        Pageable pageable = PageRequest.of(0, limit);

        List<RetryableTask> tasks = retryableTaskRepository.findRetryableTasksForProcessing(
                type,
                RetryableTaskStatus.IN_PROGRESS,
                pageable,
                Instant.now()
        );

        for(RetryableTask task : tasks){
            task.setRetryTime(currTime.plus(Duration.ofSeconds(timeoutInSeconds)));
        }

        return tasks;
    }

    @Transactional
    public void markRetryableTaskAsCompleted(List<RetryableTask> retryableTasks) {
        for(RetryableTask task : retryableTasks){
            task.setStatus(RetryableTaskStatus.SUCCESS);
        }
        retryableTaskRepository.saveAll(retryableTasks);
    }

}
