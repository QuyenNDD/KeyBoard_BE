package com.project.keyboard.repository.user;

import com.project.keyboard.dto.response.user.UserDTO;
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
    public List<UserDTO> getListUser(){
        try {
            String sql = "SELECT " +
                    "user_id, username, email, full_name, phone, address, status, created_at" +
                    " FROM users where user_id != 1";
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getInt("user_id"));
                dto.setUsername(rs.getString("username"));
                dto.setEmail(rs.getString("email"));
                dto.setFullName(rs.getString("full_name"));
                dto.setPhone(rs.getString("phone"));
                dto.setAddress(rs.getString("address"));
                dto.setStatus(rs.getBoolean("status"));
                dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return dto;
            });
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
