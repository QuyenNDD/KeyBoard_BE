package com.project.keyboard.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private int variantId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    private String color;

    @Column(precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Lob
    @Column(name = "img")
    private String img;

    private String sku;

}
