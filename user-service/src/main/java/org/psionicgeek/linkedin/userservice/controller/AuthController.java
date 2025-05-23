package org.psionicgeek.linkedin.userservice.controller;


import lombok.RequiredArgsConstructor;
import org.psionicgeek.linkedin.userservice.dto.LoginRequestDto;
import org.psionicgeek.linkedin.userservice.dto.LoginResponseDto;
import org.psionicgeek.linkedin.userservice.dto.SignupRequestDto;
import org.psionicgeek.linkedin.userservice.dto.UserDto;
import org.psionicgeek.linkedin.userservice.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto signupRequest) {

        UserDto user = authService.signup(signupRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }
}
