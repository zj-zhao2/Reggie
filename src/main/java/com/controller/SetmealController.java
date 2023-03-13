package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.Result;
import com.domain.Category;
import com.domain.Setmeal;
import com.domain.SetmealDish;
import com.dto.SetmealDto;
import com.service.CategoryService;
import com.service.SetmealDishService;
import com.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);

        String key = "setmeal_" + setmealDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return Result.success("新增套餐成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> dtoRecords = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dtoRecords);
        return Result.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteSetmeal(ids);

        Set keys = redisTemplate.keys("setmeal*");
        redisTemplate.delete(keys);

        return Result.success("套餐删除成功");
    }

    /**
     * 停售
     *
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("/status/0")
    public Result<String> statusStop(Long[] ids) {
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(0);
            setmealService.updateById(setmeal);

            String key = "setmeal_" + setmeal.getCategoryId() + "_1";
            redisTemplate.delete(key);
        }
        return Result.success("");
    }

    /**
     * 起售
     *
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("/status/1")
    public Result<String> statusContinue(Long[] ids) {
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(1);
            setmealService.updateById(setmeal);

            String key = "setmeal_" + setmeal.getCategoryId() + "_1";
            redisTemplate.delete(key);

        }
        return Result.success("");
    }

    /**
     * 获取套餐信息
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<SetmealDto>> list(Setmeal setmeal) {
        List<SetmealDto> setmealDtos = null;
        String key = "setmeal_" + setmeal.getCategoryId()+"_1";
        setmealDtos = (List<SetmealDto>) redisTemplate.opsForValue().get(key);
        if (setmealDtos != null) {
            return Result.success(setmealDtos);
        }

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, 1);
        List<Setmeal> setmeals = setmealService.list(queryWrapper);

        setmealDtos = setmeals.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> dishList = setmealDishService.list(wrapper);
            setmealDto.setSetmealDishes(dishList);
            return setmealDto;
        }).collect(Collectors.toList());

        redisTemplate.opsForValue().set(key, setmealDtos, 60, TimeUnit.MINUTES);

        return Result.success(setmealDtos);
    }
}
