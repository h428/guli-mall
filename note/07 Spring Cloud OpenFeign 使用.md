



# Feign 简介



Feign 是一个声明式的 HTTP 客户端，它的目的就是让远程调用更加简单。Feign 提供了 HTTP 请求的模板， 通过编写简单的接口和插入注解，就可以定义好 HTTP 请求的参数、格式、地址等信息。

Feign 整合了 Ribbon（负载均衡）和 Hystrix（服务熔断），可以让我们不再需要显式地使用这两个组件。Spring Cloud Feign 在 Netflix Feign 的基础上扩展了对 Spring MVC 注解的支持，在其实现下，我们只需创建一个接口并用注解的方式来配置它，即可完成对服务提供方的接口绑定。简化了 Spring Cloud Ribbon 自行封装服务调用客户端的开发量。



# 业务服务集成 Spring Cloud OpenFeign

我们以“用户查看自己可用的优惠券”这一功能为例介绍 spring cloud open-feign 的集成。该功能服务消费者为用户微服务 mall-member，服务提供者为优惠券微服务 mall-coupon。



首先为所有服务消费者引入 spring-cloud-starter-openfeign 依赖，由于服务之间会互相作为提供者和消费者，理论上所有业务微服务都需要引入 feign 进行服务调用，同时也需要引入 web 启动器以 web 形式提供服务，因此我们统一将 spring-cloud-starter-openfeign 和 spring-boot-starter-web 的依赖编写在 common 中，api 模块通过依赖 common 来引入 feign 并在接口上使用 feign 相关注解，impl 通过依赖 api 而级联引用到相关必备依赖。

```xml
<!--nacos：注册到注册中心-->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<!--feign：调用其他业务微服务-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<!--web：作为服务提供者提供服务-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

实际上，对于各个业务微服务，上述三个注解是成套的，只要是业务微服务都需要引入上述注解，可以分别对业务微服务引入，也可以统一写在 common 中（不过会增加 common 的依赖程度）。



## 服务提供者



对服务提供者 mall-coupon，我们现在 mall-coupon-api 中定义接口格式：

```java
@FeignClient("mall-coupon")
public interface CouponApi {

    String PREFIX = "/coupon/coupon";

    @GetMapping(PREFIX + "/member/list")
    R memberCoupons();
    
}
```

然后在实现模块中实现接口：

```java
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
}
```



## 服务消费者



在服务消费者 mall-member-impl 中引入 mall-coupon-api 依赖，有了依赖才能进行调用：

```xml
<dependency>
  <groupId>com.guli.mall</groupId>
  <artifactId>mall-coupon-api</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

但我们知道，各个微服务都配置在各自的域名下，比如 CouponApi 是配置在 coupon 包名下，而我们 mall-member 在 member 包名下，启动类是扫描不到 CouponApi 的，因此还需要使用 `@EnableFeignClients` 指定 api 接口所在位置，这样 Spring 才能扫描到。



同时，这个配置实际上对所有的业务微服务都是必要的，因此我们将配置统一抽取出来放到 common 模块中，最后由各个微服务的启动类统一导入即可。



首先，在 common 模块中坚一个 config 目录，统一存放通用配置，同时创建一个 CommonConfig.java 作为通用配置类扫描入口：

```java
@SpringBootConfiguration
@ComponentScan // 扫描当前 config 及子包下的所有配置
public class CommonConfig {


}
```

由于 CommonConfig 配置了扫描，我们只需在 config 目录配置各个通用配置即可，我们创建 FeignConfig.java 配置通用的 Feign 配置：

```java
// 扫描各个微服务下的 api 子包获得 feign 接口
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
```

最后，在各个微服务启动类使用 @Import 注解统一引入 CommonConfig 即可，例如以 mall-member 为例就是 MallMemberApplication.java 引入  CommonConfig：

```java
@MapperScan("com.guli.mall.member.dao")
@SpringBootApplication
@EnableDiscoveryClient
@Import(CommonConfig.class) // 此处统一引入通用配置
public class MallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallMemberApplication.class, args);
    }

}
```

编写 MemberController 的简单测试方法，访问 http://localhost:8000/member/member/coupons 测试查询成员以及优惠券列表，验证服务调用能够成功：

```java
@RestController
@RequestMapping("member/member")
public class MemberController {

    @Autowired
    private CouponApi couponApi;

    @GetMapping("coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");
        R memberCoupons = couponApi.memberCoupons();
        return R.ok().put("member", memberEntity).put("coupons", memberCoupons.get("coupons"));
    }
}
```

