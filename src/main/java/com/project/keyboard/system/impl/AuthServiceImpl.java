package com.project.keyboard.system.impl;

import com.project.keyboard.config.JwtTokenUtil;
import com.project.keyboard.dto.request.RegisterRequest;
import com.project.keyboard.dto.response.auth.AuthResponse;
import com.project.keyboard.entity.Users;
import com.project.keyboard.repository.user.UserRepository;
import com.project.keyboard.system.AuthService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public AuthResponse login(String username, String password) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public void register(RegisterRequest request){
        if(userRepository.existByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.existByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setStatus(true);
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setCreatedAt(LocalDateTime.now());
        user.setAdmin(false);

        userRepository.save(user);

    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        Claims claims = jwtTokenUtil.validateToken(refreshToken);
        String username = claims.getSubject();
        boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Integer userId = claims.get("userId", Integer.class);

        Users user = new Users();
        user.setUsername(username);
        user.setAdmin(isAdmin);
        user.setUserId(userId);

        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);

        return new AuthResponse(accessToken, newRefreshToken);
    }
}
