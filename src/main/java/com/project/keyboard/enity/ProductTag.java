package com.project.keyboard.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_tags")
public class ProductTag {
    @EmbeddedId
    private ProductTagId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("tagName")
    @JoinColumn(name = "tag_name")
    private Tag tag;
}
