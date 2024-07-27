package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.SignUpDto;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityFactory {

    public UserEntity makeUserEntity(SignUpDto signUpDto) {
        return UserEntity
                .builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .login(signUpDto.getLogin())
                .build();
    }
}
