package org.schizoscript.backend.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.UserDto;
import org.schizoscript.backend.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider {

    @Value("${user.authentication.secretKey}")
    private String secretKey;

    private final UserService userService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login) {
        long now = System.currentTimeMillis();
        final long tokenExpirationInHours = 24L;

        return Jwts
                .builder()
                .setSubject(login)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60 * tokenExpirationInHours))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication tokenIsValid(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJwt(token)
                .getBody();

        UserDto userDto = userService.findByLogin(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
