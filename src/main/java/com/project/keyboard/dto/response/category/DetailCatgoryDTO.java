package com.project.keyboard.dto.response.category;

import lombok.Data;

@Data
public class DetailCatgoryDTO {
    private String name;
    private Integer parentId;
    private String description;
}
