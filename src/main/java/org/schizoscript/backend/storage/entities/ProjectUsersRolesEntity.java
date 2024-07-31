package org.schizoscript.backend.storage.entities;

import jakarta.persistence.*;
import lombok.*;
import org.schizoscript.backend.storage.enums.ProjectRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_roles")
public class ProjectUsersRolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "project_user_id")
    private ProjectUsersRelationEntity projectUsersRelation;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;


}
