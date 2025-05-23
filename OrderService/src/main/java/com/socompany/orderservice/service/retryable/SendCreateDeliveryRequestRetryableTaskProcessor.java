package com.socompany.orderservice.service.retryable;

import com.socompany.orderservice.event.OrderCreatedEvent;
import com.socompany.orderservice.mapper.RetryableTaskMapper;
import com.socompany.orderservice.persistant.entity.RetryableTask;
import com.socompany.orderservice.service.RetryableTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendCreateDeliveryRequestRetryableTaskProcessor implements RetryableTaskProcessor {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final RetryableTaskMapper retryableTaskMapper;
    private final RetryableTaskService retryableTaskService;

    @Override
    public void processRetryableTasks(List<RetryableTask> tasks) {
        List<RetryableTask> successRetryableTasks = new ArrayList<>();
        log.info("Processing {} retryable tasks", tasks.size());
        for (RetryableTask task : tasks) {
            var isSuccess = processRetryableTask(task);
            if(isSuccess) successRetryableTasks.add(task);
        }
        log.info("Successfully processed {} retryable tasks", successRetryableTasks.size());
        retryableTaskService.markRetryableTaskAsCompleted(successRetryableTasks);
    }

    private boolean processRetryableTask(RetryableTask task) {
        log.info("Processing retryable task: {}", task);
        var order = task.getOrder();

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setDeliverAddress(order.getDeliveryAddress());
        orderCreatedEvent.setPaymentMethod(order.getPaymentMethod());
        orderCreatedEvent.setUuid(order.getId());
        orderCreatedEvent.setInventoryId(order.getInventoryId());

        log.info("Sending order created event: {}", orderCreatedEvent);

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                kafkaTemplate.send("order-created-events", orderCreatedEvent);

        log.info("Successfully sent create payment request to kafka");

        return true;

    }


}
