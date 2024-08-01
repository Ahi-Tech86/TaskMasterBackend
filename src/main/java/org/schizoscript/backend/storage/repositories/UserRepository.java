package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.dtos.project.ProjectUsersDto;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    @Query(
            "SELECT new org.schizoscript.backend.dtos.project.ProjectUsersDto(u.login, u.firstName, u.lastName, pu.projectRole) " +
            "FROM UserEntity u " +
            "INNER JOIN ProjectMemberEntity pu ON u.id = pu.userId " +
            "WHERE pu.projectId = :projectId"
    )
    List<ProjectUsersDto> findProjectUsersByProjectId(@Param("projectId") Long projectId);
}
