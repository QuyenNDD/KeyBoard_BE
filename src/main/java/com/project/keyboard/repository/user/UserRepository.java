package com.project.keyboard.repository.user;

import com.project.keyboard.entity.Users;

import java.util.List;

public interface UserRepository {
    List<Users> getListUser();
    Users findById(int userId);
    void updateStatusUser(Users users);
}
