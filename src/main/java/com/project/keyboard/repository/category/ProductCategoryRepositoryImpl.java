package com.project.keyboard.repository.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.keyboard.dto.response.category.ChildenCategoryDTO;
import com.project.keyboard.dto.response.category.DetailCatgoryDTO;
import com.project.keyboard.dto.response.category.ProductCategoryDTO;
import com.project.keyboard.entity.ProductCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<ProductCategoryDTO> getListProductCategory() {
        try {
            String sql = """
                    SELECT
                          c.category_id AS parent_id,
                          c.name AS parent_name,
                          JSON_ARRAYAGG(
                             JSON_OBJECT(
                                         'id', cc.category_id,
                                         'name', cc.name
                                         )
                             ) AS children
                          FROM product_categories c
                          LEFT JOIN product_categories cc ON cc.parent_id = c.category_id
                          WHERE c.parent_id IS NULL
                          GROUP BY c.category_id, c.name;
                    
                """;

            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                ProductCategoryDTO dto = new ProductCategoryDTO();
                dto.setCategoryId(rs.getInt("parent_id"));
                dto.setParentName(rs.getString("parent_name"));

                String childrenJson = rs.getString("children");
                if (childrenJson != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<ChildenCategoryDTO> children =
                                mapper.readValue(childrenJson, new TypeReference<List<ChildenCategoryDTO>>() {});
                        dto.setChildren(children);
                    } catch (Exception e) {
                        dto.setChildren(new ArrayList<>());
                    }
                } else {
                    dto.setChildren(new ArrayList<>());
                }

                return dto;
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

    @Override
    public DetailCatgoryDTO getDetailCategory(int id){
        try {
            String sql = """
                    SELECT c.description, c.parent_id as parentId, c.name
                    FROM product_categories as c
                    where c.category_id = ?;
                    """;
            DetailCatgoryDTO dto = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                DetailCatgoryDTO dt = new DetailCatgoryDTO();
                dt.setDescription(rs.getString("description"));
                dt.setName(rs.getString("name"));
                dt.setParentId(rs.getInt("parentId"));
                return dt;
            });
            return dto;

        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
