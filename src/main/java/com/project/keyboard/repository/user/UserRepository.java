package com.project.keyboard.repository.user;

import com.project.keyboard.dto.response.user.UserDTO;
import com.project.keyboard.entity.Users;

import java.util.List;

public interface UserRepository {
    List<UserDTO> getListUser(int page, int size);
    UserDTO getUserDetail(int userId);
    int countUsers();
    Users findById(int userId);
    void updateStatusUser(Users users);
    Users findByUsername(String username);
    Users findByEmail(String email);
    void save(Users users);
    boolean existByUsername(String username);
    boolean existByEmail(String email);
}
