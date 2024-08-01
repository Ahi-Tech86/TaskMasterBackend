package org.schizoscript.backend.dtos.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schizoscript.backend.storage.enums.ProjectRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUsersDto {
    private String login;
    private String firstname;
    private String lastname;
    private ProjectRole role;
}
