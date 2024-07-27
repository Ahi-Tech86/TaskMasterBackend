package org.schizoscript.backend.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private String firstName;

    private String lastName;

    private String login;

    private String password;

}
