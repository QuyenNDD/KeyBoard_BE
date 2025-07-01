package com.project.keyboard.repository.user;

import com.project.keyboard.entity.Users;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
            String query = "SELECT * FROM users where user_id != 1";
            return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Users.class));
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Users findById(int userId){
        try {
            String query = "SELECT * FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Users.class), userId);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateStatusUser(Users users){
        try{
            String query = "UPDATE users SET status = ? WHERE user_id = ?";
            jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Users.class), users.isStatus(), users.getUserId());
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
