package com.socompany.orderservice.persistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetryableTaskStatus {
    IN_PROGRESS("IN PROGRESS"),
    SUCCESS("SUCCESS");

    private String value;

    public static RetryableTaskStatus fromValue(String value) {
        for (RetryableTaskStatus status : RetryableTaskStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}
