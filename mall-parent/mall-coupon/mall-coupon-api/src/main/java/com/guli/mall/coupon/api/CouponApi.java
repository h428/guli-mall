package com.guli.mall.coupon.api;

import com.guli.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("mall-coupon")
public interface CouponApi {

    String PREFIX = "/coupon/coupon";

    @GetMapping(PREFIX + "/member/list")
    R memberCoupons();

}
