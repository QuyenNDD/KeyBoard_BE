package com.project.keyboard.dto;

import lombok.Data;

@Data
public class PriceOption {
    private String priceOption;
    private String querySql;

    public PriceOption(String queryParamValue, String querySql) {
        this.priceOption = queryParamValue;
        this.querySql = querySql;
    }
}
