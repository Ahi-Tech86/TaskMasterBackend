package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.SingUpRequestDto;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.enums.GlobalRole;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserEntityFactory {

    public UserEntity makeUserEntity(SingUpRequestDto singUpRequestDto) {
        Set<GlobalRole> roles = new HashSet<>();
        roles.add(GlobalRole.USER_ROLE);

        return UserEntity
                .builder()
                .firstName(singUpRequestDto.getFirstName())
                .lastName(singUpRequestDto.getLastName())
                .login(singUpRequestDto.getLogin())
                .roles(roles)
                .build();
    }
}
