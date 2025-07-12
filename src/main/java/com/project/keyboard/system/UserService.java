package com.project.keyboard.system;
import com.project.keyboard.dto.response.user.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getListUser(int page, int size);
    UserDTO getUserDetail(int userId);
    int countUsers();
    void banUser(int userId);
    void unBanUser(int userId);

}
