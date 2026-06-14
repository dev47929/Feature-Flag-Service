package com.dev.featureflag.controller;

import com.dev.featureflag.config.JwtService;
import com.dev.featureflag.dto.auth.LoginReqDto;
import com.dev.featureflag.dto.auth.LoginResponseDto;
import com.dev.featureflag.dto.auth.SignupReqDto;
import com.dev.featureflag.dto.auth.SignupResponseDTO;
import com.dev.featureflag.entity.User;
import com.dev.featureflag.repository.UserRepo;
import com.dev.featureflag.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        if (userRepo.existsByUsername(request.get("username"))) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setName(request.get("name"));
        user.setEmail(request.get("email"));
        user.setCreatedAt(LocalDateTime.now());

        userRepo.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginReqDto request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(authService.login(request, httpServletRequest));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupReqDto request) {
        return ResponseEntity.ok(authService.signup(request));
    }
}
