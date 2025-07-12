package com.project.keyboard.system;

import com.project.keyboard.dto.request.RegisterRequest;
import com.project.keyboard.dto.response.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(String email, String password);
    void register(RegisterRequest request);
    AuthResponse refreshToken(String refreshToken);
}
