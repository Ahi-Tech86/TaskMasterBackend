package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.UserDto;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoFactory {

    public UserDto makeUserDto(UserEntity userEntity) {

        return UserDto
                .builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .login(userEntity.getLogin())
                .build();
    }
}
