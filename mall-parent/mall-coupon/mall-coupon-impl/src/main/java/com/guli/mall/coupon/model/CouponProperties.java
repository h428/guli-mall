package com.guli.mall.coupon.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "coupon")
@Component
@Data
public class CouponProperties {

    private String name;

    private int age;

}
