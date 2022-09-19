package com.guli.mall.product.converter;

import cn.hutool.core.bean.BeanUtil;
import com.guli.mall.product.entity.CategoryEntity;
import com.guli.mall.product.vo.CategoryVO;

public class CategoryConverter {

    public static CategoryVO entityToVo(CategoryEntity entity) {
        final CategoryVO categoryVO = new CategoryVO();
        BeanUtil.copyProperties(entity, categoryVO);
        return categoryVO;
    }

}
