package org.schizoscript.backend.storage.entities;

import jakarta.persistence.*;
import lombok.*;
import org.schizoscript.backend.storage.enums.CompletedTaskStatus;
import org.schizoscript.backend.storage.enums.TaskPriority;

import java.time.Instant;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "archived_tasks")
public class CompletedTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "issued_id")
    private Long issuedId;

    @Column(name = "create_at")
    private Instant createAt = Instant.now();

    @Column(name = "end_at")
    private Instant endAt;

    @Column(name = "project_id")
    private Long projectId;

    @Enumerated(EnumType.STRING)
    private CompletedTaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
}
