package com.guli.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.mall.common.utils.PageUtils;
import com.guli.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author lyh
 * @email ${email}
 * @date 2022-08-14 18:54:39
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

