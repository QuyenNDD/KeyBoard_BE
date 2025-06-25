package com.project.keyboard.repository.user;

import com.project.keyboard.enity.Users;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<Users> getListUser(){
        try {
            String query = "SELECT * FROM users";
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Users.class));
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
