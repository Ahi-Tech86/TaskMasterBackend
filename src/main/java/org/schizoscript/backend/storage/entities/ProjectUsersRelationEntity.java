package org.schizoscript.backend.storage.entities;

import jakarta.persistence.*;
import lombok.*;
import org.schizoscript.backend.storage.enums.ProjectRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_users")
public class ProjectUsersRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(mappedBy = "projectUsers", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProjectUsersRolesEntity projectUsersRoles;
}
