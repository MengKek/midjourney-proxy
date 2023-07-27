package com.github.novicezk.midjourney.mapper;

import com.github.novicezk.midjourney.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    // Insert a new user record
    int insert(User user);

    // Update an existing user record
    int update(User user);

    // Delete a user record by ID
    int deleteById(Long id);

    // Select a user record by ID
    User selectById(Long id);

    // Select all user records
    List<User> selectAll();
}
