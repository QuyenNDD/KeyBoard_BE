package com.project.keyboard.repository.productVariant;

import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.entity.Product;
import com.project.keyboard.entity.ProductVariant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void deleteById(int variantId) {
        try {
            String query = "DELETE FROM product_variants WHERE variant_id = ?";
            jdbcTemplate.update(query, variantId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ProductVariant findById(int variantId){
        try {
            String sql = """
            SELECT pv.variant_id, pv.color, pv.stock_quantity, pv.price,
                   p.product_id, p.name as product_name
            FROM product_variants pv
            JOIN products p ON pv.product_id = p.product_id
            WHERE pv.variant_id = ?
        """;

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                ProductVariant variant = new ProductVariant();
                variant.setVariantId(rs.getInt("variant_id"));
                variant.setColor(rs.getString("color"));
                variant.setStockQuantity(rs.getInt("stock_quantity"));
                variant.setPrice(rs.getBigDecimal("price"));

                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));

                variant.setProduct(product);
                return variant;
            }, variantId);
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

    @Override
    public List<ProductVariant> findByProductId(int productId) {
        try {
            String sql = "SELECT * FROM product_variants WHERE product_id = ?";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductVariant.class), productId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Integer getStockByVariantId(int variantId){
        try {
            String sql = "SELECT stock_quantity FROM product_variants WHERE variant_id = ?";
            Integer stock = jdbcTemplate.queryForObject(sql, Integer.class, variantId);
            return stock == null ? 0 : stock;
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
