package com.guli.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.mall.common.utils.PageUtils;
import com.guli.mall.product.entity.CategoryEntity;

import com.guli.mall.product.vo.CategoryVO;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author lyh
 * @email ${email}
 * @date 2022-08-13 00:34:21
 */
public interface CategoryService extends IService<CategoryEntity> {

    void removeMenuByIds(List<Long> idList);

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryVO> listTree();



}

