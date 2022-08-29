package com.guli.mall.coupon.controller;

import com.guli.mall.coupon.model.CouponProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@RefreshScope
public class TestController {

    @Value("${common.name}")
    private String name;

    @Value("${common.age}")
    private int age;

    @GetMapping("name")
    public String name() {
        return name;
    }

    @GetMapping("age")
    public int age() {
        return age;
    }
}
