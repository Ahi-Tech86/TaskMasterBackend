package org.schizoscript.backend.dtos.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schizoscript.backend.storage.enums.ProjectRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberDto {
    private Long id;
    @JsonProperty("project_id")
    private Long projectId;
    private ProjectRole role;
    @JsonProperty("user_id")
    private Long userId;
}
