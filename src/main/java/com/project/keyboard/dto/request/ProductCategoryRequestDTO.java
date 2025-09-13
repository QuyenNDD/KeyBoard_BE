package com.project.keyboard.dto.request;

import lombok.Data;

@Data
public class ProductCategoryRequestDTO {
    private String name;
    private Integer parentName;
    private String description;
}
