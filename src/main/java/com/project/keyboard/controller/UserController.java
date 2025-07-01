package com.project.keyboard.controller;

import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.entity.Users;
import com.project.keyboard.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getListUser")
    public ResponseEntity<ApiResponse<List<Users>>> getListUser(){
        try {
            List<Users> users = userService.getListUser();
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách người dùng thành công", 200, "success", users, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PostMapping("/banUser/{userId}")
    public ResponseEntity<ApiResponse<Void>> banUser(@PathVariable int userId){
        try {
            userService.banUser(userId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Khóa tài khoản thành công", 200, "success", null, null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PostMapping("/unBanUser/{userId}")
    public ResponseEntity<ApiResponse<Void>> unBanUser(@PathVariable int userId){
        try {
            userService.unBanUser(userId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Mở tài khoản thành công", 200, "success", null, null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }
}
