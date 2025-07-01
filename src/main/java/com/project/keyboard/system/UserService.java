package com.project.keyboard.system;

import com.project.keyboard.entity.Users;

import java.util.List;

public interface UserService {
    List<Users> getListUser();
    void banUser(int userId);
    void unBanUser(int userId);
}
