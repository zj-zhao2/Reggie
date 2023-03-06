package com.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Orders;
import com.domain.OrdersDto;

public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);

    public Page<OrdersDto> list(int page, int pageSize);
}
