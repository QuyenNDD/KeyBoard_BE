package com.project.keyboard.system.impl;

import com.project.keyboard.dto.response.user.UserDTO;

import com.project.keyboard.entity.Users;
import com.project.keyboard.repository.user.UserRepository;
import com.project.keyboard.system.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> getListUser(int page, int size){
        return userRepository.getListUser(page, size);
    }

    @Override
    public UserDTO getUserDetail(int userId){
        return userRepository.getUserDetail(userId);
    }

    @Override
    public int countUsers(){
        return userRepository.countUsers();
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
