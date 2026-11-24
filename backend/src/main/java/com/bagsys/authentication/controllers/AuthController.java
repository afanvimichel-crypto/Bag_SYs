package com.bagsys.authentication.controllers;

import com.bagsys.authentication.dto.CurrentUserResponse;
import com.bagsys.authentication.dto.LoginRequest;
import com.bagsys.authentication.dto.LoginResponse;
import com.bagsys.authentication.services.JwtTokenService;
import com.bagsys.authentication.services.JwtTokenService.IssuedToken;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthController(
            AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken.unauthenticated(
                                request.username().trim(), request.password()));
        IssuedToken token = jwtTokenService.issue(authentication);
        long expiresIn = Math.max(0, token.expiresAt().getEpochSecond() - Instant.now().getEpochSecond());
        return new LoginResponse(
                token.value(), "Bearer", expiresIn, authentication.getName(), token.roles());
    }

    @GetMapping("/me")
    public CurrentUserResponse currentUser(@AuthenticationPrincipal Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return new CurrentUserResponse(jwt.getSubject(), roles == null ? List.of() : roles);
    }
}
