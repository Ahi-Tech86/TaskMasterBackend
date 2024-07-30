package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.CredentialsDto;
import org.schizoscript.backend.dtos.SingUpRequestDto;
import org.schizoscript.backend.dtos.UserDto;
import org.schizoscript.backend.exceptions.AppException;
import org.schizoscript.backend.factories.UserDtoFactory;
import org.schizoscript.backend.factories.UserEntityFactory;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoFactory userDtoFactory;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityFactory userEntityFactory;

    public UserDto login(CredentialsDto credentialsDto) {
        UserEntity user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userDtoFactory.makeUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SingUpRequestDto singUpRequestDto) {
        Optional<UserEntity> optionalUser = userRepository.findByLogin(singUpRequestDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userEntityFactory.makeUserEntity(singUpRequestDto);
        user.setPassword(passwordEncoder.encode(singUpRequestDto.getPassword()));

        UserEntity savedUser = userRepository.save(user);

        return userDtoFactory.makeUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userDtoFactory.makeUserDto(user);
    }
}
