package com.project.keyboard.repository.user;

import com.project.keyboard.dto.response.user.UserDTO;
import com.project.keyboard.entity.Users;

import java.util.List;

public interface UserRepository {
    List<UserDTO> getListUser();
    Users findById(int userId);
    void updateStatusUser(Users users);
}
