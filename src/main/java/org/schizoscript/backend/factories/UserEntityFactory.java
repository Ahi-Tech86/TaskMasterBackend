package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.SignUpDto;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.enums.GlobalRole;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserEntityFactory {

    public UserEntity makeUserEntity(SignUpDto signUpDto) {
        Set<GlobalRole> roles = new HashSet<>();
        roles.add(GlobalRole.USER_ROLE);

        return UserEntity
                .builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .login(signUpDto.getLogin())
                .roles(roles)
                .build();
    }
}
