package com.syncd.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoomPermission {
    @JsonProperty("write")
    WRITE,
    @JsonProperty("read")
    READ,
}