package com.guli.mall.common.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootConfiguration
@EnableFeignClients(basePackages = {
    "com.guli.mall.coupon.api",
    "com.guli.mall.member.api",
    "com.guli.mall.order.api",
    "com.guli.mall.product.api",
    "com.guli.mall.ware.api",
})
public class FeignConfig {



}
