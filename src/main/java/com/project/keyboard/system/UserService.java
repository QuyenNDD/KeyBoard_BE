package com.project.keyboard.system;

import com.project.keyboard.dto.response.user.UserDTO;
import com.project.keyboard.entity.Users;

import java.util.List;

public interface UserService {
    List<UserDTO> getListUser();
    void banUser(int userId);
    void unBanUser(int userId);
}
