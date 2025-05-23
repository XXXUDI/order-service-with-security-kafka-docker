package com.socompany.orderservice.persistant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetryableTaskType {

    SEND_CREATE_NOTIFICATION_REQUEST("SEND CREATE NOTIFICATION REQUEST"),
    SEND_CREATE_DELIVERY_REQUEST ("SEND CREATE DELIVERY REQUEST");

    private String value;

    public static RetryableTaskType fromValue(String value) {
        for (RetryableTaskType type : RetryableTaskType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
