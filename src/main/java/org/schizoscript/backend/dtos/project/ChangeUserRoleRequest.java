package org.schizoscript.backend.dtos.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserRoleRequest {
    private String login;
    private String newRoleName;
}
