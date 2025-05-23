package com.socompany.orderservice.service.retryable;

import com.socompany.orderservice.persistant.entity.RetryableTask;

import java.util.List;

public interface RetryableTaskProcessor {
    void processRetryableTasks(List<RetryableTask> tasks);
}
