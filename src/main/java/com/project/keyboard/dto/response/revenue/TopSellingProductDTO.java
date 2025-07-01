package com.project.keyboard.dto.response.revenue;

import lombok.Data;

@Data
public class TopSellingProductDTO {
    private int id;
    private String name;
    private int totalSold;
    private String img;
}
