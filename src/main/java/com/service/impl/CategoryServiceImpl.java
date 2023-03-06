package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.CustomException;
import com.dao.CategoryDao;
import com.domain.Category;
import com.domain.Dish;
import com.domain.Setmeal;
import com.service.CategoryService;
import com.service.DishService;
import com.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id条件删除分类信息
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishlambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishlambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int cnt1 = dishService.count(dishlambdaQueryWrapper);
        if(cnt1>0){
            throw new CustomException("该分类已经关联菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int cnt2 = setmealService.count(setmealLambdaQueryWrapper);
        if(cnt2>0){
            throw new CustomException("该分类已经关联套餐，无法删除");
        }
        super.removeById(id);
    }

}
