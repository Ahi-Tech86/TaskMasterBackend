package org.schizoscript.backend.dtos.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {

    private String name;
    private String description;
    @JsonProperty("issued_id")
    private Long issuedId;
    @JsonProperty("deadline")
    private int deadlineInDays;
    private String priority;
}
