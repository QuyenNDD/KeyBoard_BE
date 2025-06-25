package com.project.keyboard.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private String username;
    private String password;
    private String email;

    @Column(name = "full_name")
    private String fullName;

    private String status;
    private String phone;
    private String address;

    @Column(name = "create_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
}
