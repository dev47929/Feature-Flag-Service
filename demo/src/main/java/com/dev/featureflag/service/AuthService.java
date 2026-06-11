package com.dev.featureflag.service;

import com.dev.featureflag.config.JwtService;
import com.dev.featureflag.dto.auth.LoginReqDto;
import com.dev.featureflag.dto.auth.LoginResponseDto;
import com.dev.featureflag.dto.auth.SignupReqDto;
import com.dev.featureflag.dto.auth.SignupResponseDTO;
import com.dev.featureflag.entity.UserDetails;
import com.dev.featureflag.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public SignupResponseDTO signup(SignupReqDto signupRequestDTO) {
        if (userRepo.findByUsername(signupRequestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is not available");
        }
        UserDetails newUser = new UserDetails();
        newUser.setUsername(signupRequestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        newUser.setName(signupRequestDTO.getName());
        newUser.setEmail(signupRequestDTO.getEmail());
        newUser.setCreatedAt(LocalDateTime.now());
        userRepo.save(newUser);
        return new SignupResponseDTO(signupRequestDTO.getUsername(), signupRequestDTO.getEmail());
    }

    public LoginResponseDto login(LoginReqDto loginRequestDTO, HttpServletResponse response) {
        UserDetails user;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );
            user = (UserDetails) authentication.getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtService.generateToken(user);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return new LoginResponseDto(token, user.getId());
    }
}
