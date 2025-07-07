package com.project.keyboard.repository.category;

import com.project.keyboard.entity.ProductCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<ProductCategory> getListProductCategory() {
        try {
            String sql = """
        SELECT 
            c.category_id,
            c.name,
            c.description,
            p.name AS parent_name
        FROM 
            product_categories c
        LEFT JOIN 
            product_categories p ON c.parent_id = p.category_id
    """;

            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                ProductCategory cat = new ProductCategory();
                cat.setCategoryId(rs.getInt("category_id"));
                cat.setName(rs.getString("name"));
                cat.setDescription(rs.getString("description"));

                String parentName = rs.getString("parent_name");
                if (parentName != null) {
                    ProductCategory parent = new ProductCategory();
                    parent.setName(parentName);
                    cat.setParent(parent);
                }
                return cat;
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ProductCategory findByName(String name) {
        try {
            String query = "SELECT * FROM product_categories WHERE name = ?";
            return jdbcTemplate.queryForObject(query, new Object[]{name}, new BeanPropertyRowMapper<>(ProductCategory.class));
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public boolean existsByName(String name){
        try {
            String sql = "SELECT COUNT(*) FROM product_categories WHERE name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
            return count != null && count > 0;
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int insert(ProductCategory productCategory){
        try {
            String sql = "INSERT INTO product_categories (name, parent_id, description) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, productCategory.getName(),
                    productCategory.getParent() != null ? productCategory.getParent().getCategoryId() : null,
                    productCategory.getDescription());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ProductCategory findById(int id) {
        try {
            String sql = "SELECT * FROM product_categories WHERE category_id = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ProductCategory.class), id);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int update(int id, ProductCategory category){
        try {
            String sql = "UPDATE product_categories SET name = ?, parent_id = ?, description = ? WHERE category_id = ?";
            return jdbcTemplate.update(sql,
                    category.getName(),
                    category.getParent() != null ? category.getParent().getCategoryId() : null,
                    category.getDescription(),
                    id);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int deleteById(int id) {
        try {
            String sql = "DELETE FROM product_categories WHERE category_id = ?";
            return jdbcTemplate.update(sql, id);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
