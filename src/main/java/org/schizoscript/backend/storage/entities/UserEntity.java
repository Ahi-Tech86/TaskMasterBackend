package org.schizoscript.backend.storage.entities;

import jakarta.persistence.*;
import lombok.*;
import org.schizoscript.backend.storage.enums.ApplicationRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = ApplicationRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "application_users_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<ApplicationRole> roles = new HashSet<>();
}
