package com.dev.featureflag.controller;

import com.dev.featureflag.config.JwtService;
import com.dev.featureflag.dto.auth.LoginReqDto;
import com.dev.featureflag.entity.UserDetails;
import com.dev.featureflag.repository.UserRepo;
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

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        if (userRepo.existsByUsername(request.get("username"))) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        UserDetails user = new UserDetails();
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
    public ResponseEntity<?> login(@RequestBody LoginReqDto request) {

    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

    }
}
