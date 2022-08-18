package com.guli.mall.order.dao;

import com.guli.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lyh
 * @email ${email}
 * @date 2022-08-14 18:54:39
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
