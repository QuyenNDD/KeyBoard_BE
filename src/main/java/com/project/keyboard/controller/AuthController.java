package com.project.keyboard.controller;

import com.project.keyboard.dto.request.AuthRequest;
import com.project.keyboard.dto.request.RegisterRequest;
import com.project.keyboard.dto.request.TokenRefreshRequest;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.auth.AuthResponse;
import com.project.keyboard.system.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest  request) {
        try {
            authService.register(request);
            return ResponseEntity.ok(new ApiResponse<>("Register successful", 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Register failed", 400, "error", null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse<>("Login successful", 200, "success", response, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Login failed", 401, "error", null, e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(new ApiResponse<>("Refresh token successful", 200, "success", response, null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Refresh token failed", 401, "error", null, e.getMessage()));
        }
    }
}
