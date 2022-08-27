package com.guli.mall.coupon;

import com.guli.mall.common.config.CommonConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;


@MapperScan("com.guli.mall.coupon.dao")
@SpringBootApplication
@EnableDiscoveryClient
@Import(CommonConfig.class)
public class MallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallCouponApplication.class, args);
    }

}
