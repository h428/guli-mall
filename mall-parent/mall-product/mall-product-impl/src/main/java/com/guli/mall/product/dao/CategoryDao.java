package com.guli.mall.product.dao;

import com.guli.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lyh
 * @email ${email}
 * @date 2022-08-13 00:34:21
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
