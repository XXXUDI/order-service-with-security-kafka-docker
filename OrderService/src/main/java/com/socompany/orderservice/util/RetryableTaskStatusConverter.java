package com.socompany.orderservice.util;

import com.socompany.orderservice.persistant.enums.RetryableTaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RetryableTaskStatusConverter implements AttributeConverter<RetryableTaskStatus, String> {
    @Override
    public String convertToDatabaseColumn(RetryableTaskStatus retryableTaskStatus) {
        return retryableTaskStatus == null ? null : retryableTaskStatus.getValue();
    }

    @Override
    public RetryableTaskStatus convertToEntityAttribute(String s) {
        return s == null ? null : RetryableTaskStatus.fromValue(s);
    }
}
