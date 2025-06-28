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
            String query = "SELECT * FROM product_categories";
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(ProductCategory.class));
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

}
