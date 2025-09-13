package com.project.keyboard.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FilterProductResponse {
    private int id;
    private String title;
    private BigDecimal price;
    private boolean isSoldOut;
    private List<String> images;
}
