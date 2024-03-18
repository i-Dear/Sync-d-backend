package com.syncd.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("manager")
    MANAGER,
    @JsonProperty("member")
    MEMBER
}