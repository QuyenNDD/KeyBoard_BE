package com.project.keyboard.repository.product;

import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.response.revenue.TopSellingProductDTO;
import com.project.keyboard.entity.Product;
import com.project.keyboard.entity.ProductCategory;
import com.project.keyboard.entity.ProductVariant;
import com.project.keyboard.repository.category.ProductCategoryRepository;
import com.project.keyboard.repository.productVariant.ProductVariantRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<Product> getListProduct(){
        try{
            String query = "SELECT * FROM products";
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Product.class));
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int save(Product product){
        String sql = "INSERT INTO products (name, brand, category_id, min_price, imgs, description, stock_quantity, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setString(2, product.getBrand());
            ps.setInt(3, product.getCategory().getCategoryId());
            ps.setBigDecimal(4, product.getMinPrice() != null ? product.getMinPrice() : BigDecimal.ZERO);
            ps.setString(5, product.getImgs());
            ps.setString(6, product.getDescription());
            ps.setInt(7, product.getStockQuantity());
            ps.setBoolean(8, product.isStatus());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        int productId = keyHolder.getKey().intValue();
        product.setProductId(productId);

        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
            for (ProductVariant variant : product.getVariants()) {
                variant.setProduct(product);
                variantRepository.save(variant);
            }
        }
        return productId;
    }

    @Override
    public Product findById(int id){
        try {
            String query = "SELECT * FROM products WHERE product_id = ?";
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Product.class), id);
        }catch (EmptyResultDataAccessException e){
            return null;
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean softDeleteProductById(int id){
        String query = "UPDATE products SET status = false WHERE product_id = ?";
        int rows = jdbcTemplate.update(query, id);
        return rows > 0;
    }

    @Override
    public void update(Product product){
        try {
            String sql = """
        UPDATE products
        SET name = ?, brand = ?, category_id = ?, min_price = ?, imgs = ?,\s
            description = ?, stock_quantity = ?, status = ?
        WHERE product_id = ?
   \s""";

            jdbcTemplate.update(sql,
                    product.getName(),
                    product.getBrand(),
                    product.getCategory().getCategoryId(),
                    product.getMinPrice(),         // Cập nhật nếu có thay đổi
                    product.getImgs(),             // Có thể null hoặc JSON ảnh
                    product.getDescription(),
                    product.getStockQuantity(),   // Tổng số lượng từ biến thể
                    product.isStatus(),
                    product.getProductId()
            );
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    @Override
    public void updateProduct(ProductUpdateDTO dto){

        try{
            String categoryName = dto.getCategory();

            ProductCategory category = categoryRepository.findByName(categoryName);
            if (category == null) {
                throw new RuntimeException("Danh mục sản phẩm không tồn tại");
            }
            String sql = "UPDATE products SET name = ?, brand = ?, description = ?, category_id = ?, imgs = ? WHERE product_id = ?";
            jdbcTemplate.update(sql,
                    dto.getName(),
                    dto.getBrand(),
                    dto.getDescription(),
                    category.getCategoryId(),
                    dto.getImgs(),
                    dto.getProductId());
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<TopSellingProductDTO> getTopSellingProduct(int limit){
        try {
            String sql = """
                SELECT 
                    pv.product_id AS id,
                    p.name,
                    SUM(od.quantity) AS totalSold,
                    SUBSTRING_INDEX(p.imgs, ';', 1) AS img
                FROM
                    order_details od
                JOIN
                    product_variants pv ON od.variant_id = pv.variant_id
                JOIN
                    products p ON pv.product_id = p.product_id
                JOIN
                    orders o ON od.order_id = o.order_id
                WHERE
                    o.status = 'Completed'
                GROUP BY
                    pv.product_id, p.name, p.imgs
                ORDER BY
                    totalSold DESC
                LIMIT ?
            """;

            return jdbcTemplate.query(
                    sql,
                    new Object[]{limit},
                    (rs, rowNum) -> {
                        TopSellingProductDTO dto = new TopSellingProductDTO();
                        dto.setId(rs.getInt("id"));
                        dto.setName(rs.getString("name"));
                        dto.setTotalSold(rs.getInt("totalSold"));
                        dto.setImg(rs.getString("img"));
                        return dto;
                    }
            );
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
