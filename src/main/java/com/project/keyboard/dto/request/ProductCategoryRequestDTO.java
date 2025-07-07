package com.project.keyboard.dto.request;

import lombok.Data;

@Data
public class ProductCategoryRequestDTO {
    private String name;
    private String parentName;
    private String description;
}
