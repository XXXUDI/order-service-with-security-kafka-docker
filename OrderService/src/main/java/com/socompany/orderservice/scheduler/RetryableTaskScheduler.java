package com.socompany.orderservice.scheduler;


import com.socompany.orderservice.event.OrderCreatedEvent;
import com.socompany.orderservice.mapper.RetryableTaskMapper;
import com.socompany.orderservice.persistant.enums.RetryableTaskType;
import com.socompany.orderservice.service.RetryableTaskService;
import com.socompany.orderservice.service.retryable.RetryableTaskProcessor;
import com.socompany.orderservice.service.retryable.SendCreateDeliveryRequestRetryableTaskProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class RetryableTaskScheduler {

    private final RetryableTaskService retryableTaskService;

    private final Map<RetryableTaskType, RetryableTaskProcessor> retryableTaskProcessor;

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public RetryableTaskScheduler(RetryableTaskService retryableTaskService,
                                  RetryableTaskMapper retryableTaskMapper,
                                  KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.retryableTaskService = retryableTaskService;
        this.kafkaTemplate = kafkaTemplate;
        this.retryableTaskProcessor = Map.of(
                RetryableTaskType.SEND_CREATE_DELIVERY_REQUEST, new SendCreateDeliveryRequestRetryableTaskProcessor(kafkaTemplate, retryableTaskMapper, retryableTaskService)
        );
    }
        @Scheduled(fixedRate = 25000)
        void executeRetryableTasks() {
            log.info("Executing retryable tasks");

            for (Map.Entry<RetryableTaskType, RetryableTaskProcessor> entry : retryableTaskProcessor.entrySet()) {
                var retryableTaskType = entry.getKey();
                var retryableTaskProcessor = entry.getValue();

                var retryableTask = retryableTaskService.getRetryableTasksForProcessing(retryableTaskType);

                retryableTaskProcessor.processRetryableTasks(retryableTask);
            }

            log.info("Finished executing retryable tasks");

        }
}
