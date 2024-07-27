package org.schizoscript.backend.storage.entities;

import jakarta.persistence.*;
import lombok.*;

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

//    @Column(length = 50, nullable = false)
    private String firstName;

//    @Column(length = 50, nullable = false)
    private String lastName;

//    @Column(unique = true, length = 50, nullable = false)
    private String login;

//    @Column(nullable = false)
    private String password;
}
