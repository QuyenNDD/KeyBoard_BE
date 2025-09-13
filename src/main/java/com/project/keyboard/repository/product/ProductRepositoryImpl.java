package com.project.keyboard.repository.product;

import com.project.keyboard.dto.request.ProductImgDTO;
import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.response.FilterProductResponse;
import com.project.keyboard.dto.response.product.NewProductDTO;
import com.project.keyboard.dto.response.product.ProductResponeDTO;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<Product> getListProduct(int page, int size){
        try{
            int offset = page * size;
            String sql = """
        SELECT 
            p.product_id,
            p.name,
            p.brand,
            p.min_price,
            p.description,
            p.imgs,
            pc.category_id,
            pc.name AS category_name,
            pc.description AS category_description
        FROM 
            products p
        LEFT JOIN 
            product_categories pc ON p.category_id = pc.category_id
        ORDER BY p.product_id
        LIMIT ? OFFSET ?
    """;

            return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setMinPrice(rs.getBigDecimal("min_price"));
                product.setDescription(rs.getString("description"));
                product.setImgs(rs.getString("imgs"));

                int catId = rs.getInt("category_id");
                if (catId != 0) {
                    ProductCategory category = new ProductCategory();
                    category.setCategoryId(catId);
                    category.setName(rs.getString("category_name"));
                    category.setDescription(rs.getString("category_description"));
                    product.setCategory(category);
                }
                return product;
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int countProducts(){
        try{
            String sql = "SELECT COUNT(*) FROM products";
            return jdbcTemplate.queryForObject(sql, Integer.class);
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
            Integer categoryName = dto.getCategoryId();
            ProductCategory category = categoryRepository.findById(categoryName);
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
                SELECT\s
                                       pv.product_id AS id,
                                       p.name,
                                       p.min_price,
                                       SUM(od.quantity) AS totalSold,
                                       p.imgs
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
                        dto.setPrice(rs.getBigDecimal("min_price"));
                        dto.setTotalSold(rs.getInt("totalSold"));
                        String imgsStr = rs.getString("imgs");
                        List<String> imgs = new ArrayList<>();
                        if (imgsStr != null && !imgsStr.isBlank()) {
                            String[] parts = imgsStr.split(",");
                            if (parts.length >= 2) {
                                imgs.add(parts[0].trim());
                                imgs.add(parts[1].trim());
                            } else if (parts.length == 1) {
                                // Nếu chỉ có 1 ảnh thì nhân đôi
                                imgs.add(parts[0].trim());
                                imgs.add(parts[0].trim());
                            }
                        }
                        dto.setImages(imgs);
                        return dto;
                    }
            );
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<NewProductDTO> getNewProduct(int limit){
        try {
            String sql = """
                    SELECT product_id AS id,
                                                               name,
                                                               min_price AS price,
                                                               imgs,
                                                               (CASE WHEN stock_quantity = 0 THEN 1 ELSE 0 END) AS is_sold_out
                                                        FROM products
                                                        WHERE status = 1
                                                        ORDER BY created_at DESC
                                                        LIMIT ?
              """;
            return jdbcTemplate.query(sql, new Object[]{limit}, (rs, rowNum) -> {
                NewProductDTO dto = new NewProductDTO();
                dto.setId(rs.getInt("id"));
                dto.setName(rs.getString("name"));
                dto.setPrice(rs.getBigDecimal("price"));
                String imgsStr = rs.getString("imgs");
                List<String> imgs = new ArrayList<>();
                if (imgsStr != null && !imgsStr.isBlank()) {
                    String[] parts = imgsStr.split(",");
                    if (parts.length >= 2) {
                        imgs.add(parts[0].trim());
                        imgs.add(parts[1].trim());
                    } else if (parts.length == 1) {
                        // Nếu chỉ có 1 ảnh thì nhân đôi
                        imgs.add(parts[0].trim());
                        imgs.add(parts[0].trim());
                    }
                }
                dto.setImages(imgs);
                dto.setSoldOut(rs.getBoolean("is_sold_out"));
                return dto;
            });
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Product findProductById(int id){
        try {
            String sql = """
            SELECT 
                p.product_id,
                p.name,
                p.brand,
                p.min_price,
                p.description,
                p.imgs,
                pc.category_id,
                pc.name AS category_name,
                pc.description AS category_description
            FROM 
                products p
            LEFT JOIN 
                product_categories pc ON p.category_id = pc.category_id
            WHERE 
                p.product_id = ?
        """;

            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setBrand(rs.getString("brand"));
                product.setMinPrice(rs.getBigDecimal("min_price"));
                product.setDescription(rs.getString("description"));
                product.setImgs(rs.getString("imgs"));

                int catId = rs.getInt("category_id");
                if (catId != 0) {
                    ProductCategory category = new ProductCategory();
                    category.setCategoryId(catId);
                    category.setName(rs.getString("category_name"));
                    category.setDescription(rs.getString("category_description"));
                    product.setCategory(category);
                }

                return product;
            });

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<FilterProductResponse> filterProduct(String filterSql, int page, int size){
        try{
            int offset = page * size;
            StringBuilder sql = new StringBuilder( """
                            SELECT DISTINCT p.product_id, p.name, p.min_price, p.imgs, p.stock_quantity
                            FROM products p
                            WHERE status = 1
                            """);
            sql.append(filterSql).append("\n");
            sql.append("ORDER BY p.min_price\n" +
                    "        LIMIT ? OFFSET ?");
            return jdbcTemplate.query(sql.toString(), new Object[]{size, offset}, (rs, rowNum) -> {
                FilterProductResponse dto = new FilterProductResponse();
                dto.setId(rs.getInt("product_id"));
                dto.setTitle(rs.getString("name"));
                dto.setPrice(rs.getBigDecimal("min_price"));
                String imgsStr = rs.getString("imgs");
                List<String> imgs = new ArrayList<>();
                if (imgsStr != null && !imgsStr.isBlank()) {
                    String[] parts = imgsStr.split(",");
                    if (parts.length >= 2) {
                        imgs.add(parts[0].trim());
                        imgs.add(parts[1].trim());
                    } else if (parts.length == 1) {
                        // Nếu chỉ có 1 ảnh thì nhân đôi
                        imgs.add(parts[0].trim());
                        imgs.add(parts[0].trim());
                    }
                }
                dto.setImages(imgs);
                int stock_quantity = rs.getInt("stock_quantity");
                dto.setSoldOut(stock_quantity > 0);
                return dto;
            });
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int countFilterProduct(String filterQuery){
        try {
            StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(DISTINCT p.product_id)
                            FROM products p
                            WHERE status = 1
                    """);
            sql.append(filterQuery);
            return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
