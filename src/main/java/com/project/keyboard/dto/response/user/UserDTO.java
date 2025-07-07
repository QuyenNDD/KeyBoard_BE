package com.project.keyboard.dto.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private boolean status;
    private LocalDateTime createdAt;
}
