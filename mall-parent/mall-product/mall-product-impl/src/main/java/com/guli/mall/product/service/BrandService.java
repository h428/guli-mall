package com.guli.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.mall.common.utils.PageUtils;
import com.guli.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author lyh
 * @email ${email}
 * @date 2022-08-13 00:34:21
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

