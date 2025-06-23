package com.scotiabank.cc.mscollegeapi.enums;

import java.util.Arrays;

public enum StatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive"),
    ;

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }

    public static StatusEnum fromValue(String status) {
        return Arrays.stream(StatusEnum.values())
                .filter(s -> status.equalsIgnoreCase(s.status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado desconocido: " + status));
    }
}
