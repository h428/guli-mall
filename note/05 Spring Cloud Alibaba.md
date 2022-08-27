



#  Spring Cloud Alibaba 简介



[Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba) 致力于提供 微服务开发的 一站式解决方案。此项目包含开发分布式应用微服务的必需组件，方便开发者通过 Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务。

通过使用 Spring Cloud Alibaba，您只需要添加一些注解和少量配置，就可以将 Spring Cloud 应用
接入阿里微服务解决方案，通过阿里中间件来迅速搭建分布式应用系统。



# Spring Cloud 与 Spring Cloud Alibaba 对比



Spring Cloud  的几大痛点：

- Spring Cloud 部分组件停止维护和更新，给开发带来不便；

- Spring Cloud 部分环境搭建复杂，没有完善的可视化界面，我们需要大量的二次开发和定制

- Spring Cloud 配置复杂，难以上手，部分配置差别难以区分和合理应用

  

Spring Cloud Alibaba  的优势：

- 阿里使用过的组件经历了考验，性能强悍，设计合理，现在开源出来供大家使用
- 成套的产品搭配完善的可视化界面给开发运维带来极大的便利。
- 搭建简单，学习曲线低。



# Spring Cloud 体系版本选择



结合 Spring Cloud 和 Spring Cloud Alibaba  我们最终的技术搭配方案：

- Spring Cloud Alibaba - Nacos ：注册中心（服务发现/ 注册）
- Spring Cloud Alibaba - Nacos ：配置中心（动态配置管理）
- Spring Cloud - Ribbon ：负载均衡
- Spring Cloud - Feign ：声明式 HTTP  客户端（调用远程服务）
- Spring Cloud Alibaba - Sentinel ：服务容错（限流、降级、熔断）
- Spring Cloud - Gateway ：API  网关（webflux  编程模式）
- Spring Cloud - Sleuth ：调用链监控
- Spring Cloud Alibaba - Seata：原 Fescar



由于 Spring Boot 1 和 Spring Boot 2 在 Actuator 模块的接口和注解有很大的变更，且 spring-cloud-commons 从 1.x.x 版本升级到 2.0.0 版本也有较大的变更，因此我们采取跟 SpringBoot 版本号一致的版本。Spring Cloud Alibaba 也提供了[官方版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)供我们参考 :

- 1.5.x 版本适用于 Spring Boot 1.5.x
- 2.0.x 版本适用于 Spring Boot 2.0.x
- 2.1.x 版本适用于 Spring Boot 2.1.x

我们本次选择的版本为：Spring Boot: 2.1.13.RELEASE, Spring Cloud:Greenwich.SR6, Spring Cloud Alibaba:2.1.4.RELEASE

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>2.1.13.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>Greenwich.SR6</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-alibaba-dependencies</artifactId>
      <version>2.1.4.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

