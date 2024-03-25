package com.syncd.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("host")
    HOST,
    @JsonProperty("manager")
    MANAGER,
    @JsonProperty("member")
    MEMBER,
    @JsonProperty("guest")
    GUEST
}