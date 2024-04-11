package com.syncd.application.domain.project;

import com.syncd.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInTeam {
    private final String userId;
    private final Role role;
}
