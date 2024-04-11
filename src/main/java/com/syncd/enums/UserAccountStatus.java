package com.syncd.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserAccountStatus {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("unavailable")
    UNAVAILABLE,
}