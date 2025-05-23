package com.socompany.orderservice.util;

import com.socompany.orderservice.persistant.enums.RetryableTaskType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RetryableTaskTypeConverter implements AttributeConverter<RetryableTaskType, String> {
    @Override
    public String convertToDatabaseColumn(RetryableTaskType retryableTaskType) {
        return retryableTaskType == null ? null : retryableTaskType.getValue();
    }

    @Override
    public RetryableTaskType convertToEntityAttribute(String s) {
        return s == null ? null : RetryableTaskType.fromValue(s);
    }
}
