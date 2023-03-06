package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Setmeal;
import com.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public void deleteSetmeal(List<Long> ids);
}
