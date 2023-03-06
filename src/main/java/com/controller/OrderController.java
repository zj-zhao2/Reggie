package com.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.Result;
import com.domain.Orders;
import com.domain.OrdersDto;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return Result.success("下单成功");
    }

    /**
     * 查询订单信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> list(int page, int pageSize){

        Page<OrdersDto> dtoPage = orderService.list(page, pageSize);
        return Result.success(dtoPage);
    }
}
