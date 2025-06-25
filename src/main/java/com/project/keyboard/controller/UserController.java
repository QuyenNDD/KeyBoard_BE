package com.project.keyboard.controller;

import com.project.keyboard.enity.Users;
import com.project.keyboard.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getListUser")
    public ResponseEntity<List<Users>> getListUser(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getListUser());
    }
}
