package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectUsersRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUsersRoleRepository extends JpaRepository<ProjectUsersRolesEntity, Long> {
}
