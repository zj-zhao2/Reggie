package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.OrderDetailDao;
import com.domain.OrderDetail;
import com.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
