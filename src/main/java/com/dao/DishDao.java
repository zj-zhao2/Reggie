package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishDao extends BaseMapper<Dish> {
}
