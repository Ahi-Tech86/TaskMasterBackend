package org.schizoscript.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.configuration.UserAuthenticationProvider;
import org.schizoscript.backend.dtos.CredentialsDto;
import org.schizoscript.backend.dtos.SingUpRequestDto;
import org.schizoscript.backend.dtos.UserDto;
import org.schizoscript.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SingUpRequestDto singUpRequestDto) {
        UserDto createdUser = userService.register(singUpRequestDto);
        createdUser.setToken(userAuthenticationProvider.createToken(singUpRequestDto.getLogin()));
//        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
        return ResponseEntity.ok(createdUser);
    }
}
