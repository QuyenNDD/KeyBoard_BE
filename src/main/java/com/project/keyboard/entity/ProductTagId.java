package com.project.keyboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagId implements Serializable {

    @Column(name = "product_id")
    private int productId;

    @Column(name = "tag_name")
    private String tagName;
}
