package com.project.keyboard.repository.productVariant;

import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.entity.ProductVariant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductVariantRepositoryImpl implements ProductVariantRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    protected Log log = LogFactory.getLog(getClass());

    @Override
    public void save(ProductVariant variant) {
        String sql = """
            INSERT INTO product_variants (product_id, color, price, stock_quantity, img, sku)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                variant.getProduct().getProductId(),
                variant.getColor(),
                variant.getPrice(),
                variant.getStockQuantity(),
                variant.getImg(),
                variant.getSku()
        );
    }

    @Override
    public void deleteById(int id) {
        try {
            String query = "DELETE FROM product_variants WHERE variant_id = ?";
            jdbcTemplate.update(query, id);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ProductVariant findById(int variantId){
        try{
            String query = "SELECT * FROM product_variants WHERE variant_id = ?";
            return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(ProductVariant.class), variantId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(ProductVariant variant){
        try{
            String sql = """
            UPDATE product_variants
            SET color = ?, price = ?, stock_quantity = ?, img = ?, sku = ?
            WHERE variant_id = ?
        """;
            jdbcTemplate.update(sql,
                    variant.getColor(),
                    variant.getPrice(),
                    variant.getStockQuantity(),
                    variant.getImg(),
                    variant.getSku(),
                    variant.getVariantId()
            );
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateVariant(ProductVariantUpdateDTO v) {
        try {
            String sql = "UPDATE product_variants SET color = ?, price = ?, stock_quantity = ?, sku = ?, img = ? WHERE variant_id = ?";
            jdbcTemplate.update(sql,
                    v.getColor(),
                    v.getPrice(),
                    v.getStockQuantity(),
                    v.getSku(),
                    v.getImg(),
                    v.getVariantId());
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }
    @Override
    public void insertVariant(ProductVariantUpdateDTO v, int productId) {
        try {
            String sql = "INSERT INTO product_variants (product_id, color, price, stock_quantity, sku, img) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    productId,
                    v.getColor(),
                    v.getPrice(),
                    v.getStockQuantity(),
                    v.getSku(),
                    v.getImg());
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public void deleteVariantById(int variantId) {
        try {
            String sql = "DELETE FROM product_variants WHERE variant_id = ?";
            jdbcTemplate.update(sql, variantId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }
}
