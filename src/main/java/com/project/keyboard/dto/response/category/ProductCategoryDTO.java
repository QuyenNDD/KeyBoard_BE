package com.project.keyboard.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {
    private int categoryId;
    private String parentName;
    private List<ChildenCategoryDTO> children;
}
