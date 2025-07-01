package com.project.keyboard.system.impl;

import com.project.keyboard.entity.Users;
import com.project.keyboard.repository.user.UserRepository;
import com.project.keyboard.system.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> getListUser(){
        return userRepository.getListUser();
    }

    @Override
    public void banUser(int userId){
        Users user = userRepository.findById(userId);
        if (user == null) throw new RuntimeException("User not found");
        user.setStatus(false);
        userRepository.updateStatusUser(user);
    }

    @Override
    public void unBanUser(int userId){
        Users user = userRepository.findById(userId);
        if (user == null) throw new RuntimeException("User not found");
        user.setStatus(true);
        userRepository.updateStatusUser(user);
    }
}
