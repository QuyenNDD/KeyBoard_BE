package com.project.keyboard.dto.response.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NewProductDTO {
    private int id;
    private String name;
    private BigDecimal price;
    private List<String> images;
    private boolean isSoldOut;
}
