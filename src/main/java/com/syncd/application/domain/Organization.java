package com.syncd.application.domain;

import java.util.List;

public record Organization (
    String organizationId,
    String name,
    String description,
    List<String> workspaceIds,
    List<UsersInOrg> users
){
    record UsersInOrg(){}
}
