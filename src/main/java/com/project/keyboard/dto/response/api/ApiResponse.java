package com.project.keyboard.dto.response.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int code;
    private String status; // "success" | "error"
    private T data;
    private String stack;
}
