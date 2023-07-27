package com.github.novicezk.midjourney.mapper;

import com.github.novicezk.midjourney.domain.Turnover;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface TurnoverMapper {

    // Insert a new turnover record
    int insert(Turnover turnover);

    // Update an existing turnover record
    int update(Turnover turnover);

    // Delete a turnover record by ID
    int deleteById(Long id);

    // Select a turnover record by ID
    Turnover selectById(Long id);

    // Select all turnover records for a specific user ID
    List<Turnover> selectByUserId(Long userId);

    // Select all turnover records
    List<Turnover> selectAll();

    // Additional methods can be added based on your requirements
}
