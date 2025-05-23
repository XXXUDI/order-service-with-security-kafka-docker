package com.socompany.orderservice.persistant.entity;

import com.socompany.orderservice.persistant.enums.RetryableTaskStatus;
import com.socompany.orderservice.persistant.enums.RetryableTaskType;
import com.socompany.orderservice.util.RetryableTaskStatusConverter;
import com.socompany.orderservice.util.RetryableTaskTypeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetryableTask extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Convert (converter = RetryableTaskTypeConverter.class)
    private RetryableTaskType type;

    @Convert (converter = RetryableTaskStatusConverter.class)
    private RetryableTaskStatus status;

    private Instant retryTime;

}
