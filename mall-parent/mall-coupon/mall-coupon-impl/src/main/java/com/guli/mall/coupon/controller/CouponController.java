package com.guli.mall.coupon.controller;

import com.guli.mall.coupon.api.CouponApi;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guli.mall.coupon.entity.CouponEntity;
import com.guli.mall.coupon.service.CouponService;
import com.guli.mall.common.utils.PageUtils;
import com.guli.mall.common.utils.R;



/**
 * 优惠券信息
 *
 * @author lyh
 * @email ${email}
 * @date 2022-08-14 18:40:37
 */
@RestController
@RequestMapping(CouponApi.PREFIX)
@Slf4j
public class CouponController implements CouponApi {

    @Autowired
    private CouponService couponService;

    @GetMapping("/member/list")
    public R memberCoupons() {
        log.info("查询成员优惠券：开始...");
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("满100减10");
        List<CouponEntity> coupons = Collections.singletonList(couponEntity);
        log.info("查询成员优惠券：结束...");
        return R.ok().put("coupons", coupons);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:coupon:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:coupon:info")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:coupon:save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:coupon:update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:coupon:delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
