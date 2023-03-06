package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Dish;
import com.dto.DishDto;

public interface DishService extends IService<Dish> {

    //新增菜品，处理两张表
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(Long[] ids);
}
