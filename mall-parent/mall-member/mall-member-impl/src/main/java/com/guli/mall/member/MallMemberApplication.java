package com.guli.mall.member;

import com.guli.mall.common.config.CommonConfig;
import com.guli.mall.common.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;


@MapperScan("com.guli.mall.member.dao")
@SpringBootApplication
@EnableDiscoveryClient
@Import(CommonConfig.class)
public class MallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallMemberApplication.class, args);
    }

}
