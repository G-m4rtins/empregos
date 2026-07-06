package com.gabriel.empregos.api.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.empregos.api.auth.dtos.LoginRequest;
import com.gabriel.empregos.api.auth.dtos.TokenResponse;
import com.gabriel.empregos.api.auth.dtos.UserRequest;
import com.gabriel.empregos.api.auth.dtos.UserResponse;
import com.gabriel.empregos.api.auth.mappers.UserMapper;
import com.gabriel.empregos.core.models.User;
import com.gabriel.empregos.core.repositories.UserRepository;
import com.gabriel.empregos.core.services.jwt.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {

        var authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword());
        authenticationManager.authenticate(authentication);

        return TokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(loginRequest.getEmail()))
                .build();
    }

}
