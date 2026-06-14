package com.dev.featureflag.service;

import com.dev.featureflag.config.JwtService;
import com.dev.featureflag.dto.auth.LoginReqDto;
import com.dev.featureflag.dto.auth.LoginResponseDto;
import com.dev.featureflag.dto.auth.SignupReqDto;
import com.dev.featureflag.dto.auth.SignupResponseDTO;
import com.dev.featureflag.entity.User;
import com.dev.featureflag.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if (userRepo.existsByUsername(signupRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Username is not available");
        }

        if (userRepo.existsByEmail(signupRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User newUser = new User();
        newUser.setUsername(signupRequestDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        newUser.setName(signupRequestDTO.getName());
        newUser.setEmail(signupRequestDTO.getEmail());
        newUser.setCreatedAt(LocalDateTime.now());
        userRepo.save(newUser);
        return new SignupResponseDTO(signupRequestDTO.getUsername(), signupRequestDTO.getEmail());
    }

    public LoginResponseDto login(LoginReqDto loginRequestDTO, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        User user = (User) authentication.getPrincipal();
        return new LoginResponseDto(jwtService.generateToken(user), user.getId());
    }



}
