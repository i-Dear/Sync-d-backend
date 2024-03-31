package com.syncd.application.domain.team;

import java.util.List;

public record Team(
    String organizationId,
    String name,
    String description,
    List<String> workspaceIds,
    List<UsersInOrg> users
){
    record UsersInOrg(){}
}
