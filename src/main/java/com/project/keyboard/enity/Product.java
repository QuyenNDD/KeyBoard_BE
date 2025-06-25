package com.project.keyboard.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    private String name;

    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory categoryId;

    @Column(name = "min_price", precision = 18, scale = 2)
    private BigDecimal minPrice;

    @Lob
    @Column(name = "imgs")
    private String imgs;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "status")
    private boolean status;

    @Column(name = "create_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
}
