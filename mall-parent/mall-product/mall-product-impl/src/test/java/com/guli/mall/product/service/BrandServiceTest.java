package com.guli.mall.product.service;

import com.guli.mall.product.ProductBaseTest;
import com.guli.mall.product.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class BrandServiceTest extends ProductBaseTest {

    @Autowired
    private BrandService brandService;

    @org.junit.Test
    @Rollback(value = false) // 为了观察效果，覆盖父类的自动回滚
    public void save() {
        BrandEntity brand = new BrandEntity();
        brand.setName("小米");
        brandService.save(brand);
    }
}