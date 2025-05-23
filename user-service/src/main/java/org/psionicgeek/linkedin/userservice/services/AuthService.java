package org.psionicgeek.linkedin.userservice.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.psionicgeek.linkedin.userservice.dto.LoginRequestDto;
import org.psionicgeek.linkedin.userservice.dto.LoginResponseDto;
import org.psionicgeek.linkedin.userservice.dto.SignupRequestDto;
import org.psionicgeek.linkedin.userservice.dto.UserDto;
import org.psionicgeek.linkedin.userservice.entity.User;
import org.psionicgeek.linkedin.userservice.exception.BadRequestException;
import org.psionicgeek.linkedin.userservice.exception.ResourceNotFoundException;
import org.psionicgeek.linkedin.userservice.repository.UserRepository;
import org.psionicgeek.linkedin.userservice.utils.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signup(SignupRequestDto signupRequest) {

        boolean exists = userRepository.existsByEmail(signupRequest.getEmail());
        if (exists) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(PasswordUtil.hashPassword(signupRequest.getPassword()));

        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);

    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        boolean isPasswordValid = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if (!isPasswordValid) {
            throw new BadRequestException("Invalid email or password");
        }
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(jwtService.generateAccessToken(user));

        return loginResponseDto;

    }
}
