package com.project.keyboard.system.impl;

import com.project.keyboard.enity.Users;
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
    public List<Users> getListUser(){
        return userRepository.getListUser();
    }
}
