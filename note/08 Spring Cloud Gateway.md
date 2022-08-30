

# 简介

网关作为流量的入口，常用功能包括路由转发、权限校验、限流控制等。而 Spring Cloud Gateway 作为 Spring Cloud 官方推出的第二代网关框架，取代了 Zuul 网关。

网关提供 API 全托管服务，丰富的 API 管理功能，辅助企业管理大规模的 API，以降低管理成本和安全风险，包括协议适配、协议转发、安全策略、防刷、流量、监控日志等功能。

[Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) 旨在提供一种简单而有效的方式来对 API 进行路由，并为他们提供切面，例如：安全性，监控指标和弹性等。


Spring Cloud Gateway 特点:
- 基于 Spring5，支持响应式编程和 SpringBoot2.0
- 支持使用任何请求属性进行路由匹配
- 特定于路由的断言和过滤器
- 集成 Hystrix 进行断路保护
- 集成服务发现功能
- 易于编写 Predicates 和 Filters
- 支持请求速率限制
- 支持路径重写


思考：为什么使用 API 网关？ 答：API 网关出现的原因是微服务架构的出现，不同的微服务一般会有不同的网络地址，而外部客户端可能需要调用多个服务的接口才能完成一个业务需求，如果让客户端直接与各个微服务通信，会有以下的问题：
- 客户端会多次请求不同的微服务，增加了客户端的复杂性。
- 存在跨域请求，在一定场景下处理相对复杂。
- 认证复杂，每个服务都需要独立认证。
- 难以重构，随着项目的迭代，可能需要重新划分微服务。例如，可能将多个服务合并成一个或者将一个服务拆分成多个。如果客户端直接与微服务通信，那么重构将会很难实施。
- 某些微服务可能使用了防火墙/浏览器不友好的协议，直接访问会有一定的困难。

以上这些问题可以借助 API 网关解决。API 网关是介于客户端和服务器端之间的中间层，所有的外部请求都会先经过 API 网关这一层。也就是说，API 的实现方面更多的考虑业务逻辑，而安全、性能、监控可以交由 API 网关来做，这样既提高业务灵活性又不缺安全性。使用 API 网关后优点如下：
- 易于监控。可以在网关收集监控数据并将其推送到外部系统进行分析。
- 易于认证。可以在网关上进行认证，然后再将请求转发到后端的微服务，而无须在每个微服务中进行认证。
- 减少了客户端与各个微服务之间的交互次数。

# 核心概念

Spring Cloud 的其工作工作流程大致为：客户端发送请求给网关 Spring Cloud Gateway，网关映射器 HandlerMapping 根据各个路由的路由断言配置判断访问地址是否满足某个路由，满足的话就会采用该路由进行转发，其会将请求交给 WebHandler，这个 WebHandler 将请求交给一个过滤器链，请求到达目标服务之前，会执行所有过滤器的 pre 方法。请求到达目标服务处理之后再依次执行所有过滤器的 post 方法，工作流程大致如下图所示：
![Spring Cloud Gateway 原理图](https://raw.githubusercontent.com/h428/img/master/note/00000225.jpg)

一句话简单描述上述内容：满足某些断言（predicates）就路由到指定的地址（uri），转发过程会经过指定的过滤器（filter）。


根据上述描述，Spring Cloud Gateway 最关键的配置就是各个路由的配置，其中每个路由配置有各自的路由断言和过滤器：
- 路由：路由是网关最基础的部分，完整的路由配置包括一个 ID、一个目的 URL、一组路由断言和一组 Filter 组成。如果这组断言都为真，会采用该路由进行转发，将访问地址转发到目的 URL 下的对应地址（相当于前缀替换），期间会经过一系列 Filter。
- 路由断言：Java8 中的断言函数，Spring Cloud Gateway 中的断言函数输入类型是 Spring5.0 框架中的 ServerWebExchange。Spring Cloud Gateway 中的断言函数允许开发者去定义匹配来自于 http request 中的任何信息，比如请求头和参数等，只有断言成功的 URL 才会采用该路由进行转发。
- 过滤器：一个标准的 Spring webFilter，Spring cloud gateway 中的 filter 分为两种类型的
Filter，分别是 Gateway Filter 和 Global Filter。过滤器 Filter 将会对请求和响应进行修改
处理。


# 配置并使用 Spring Cloud Gateway

我们创建 mall-gateway 工程作为网关，网关需要引入下列配置，有一点需要注意，Spring Cloud Gateway 底层基于 Netty 容器编写，依赖 Netty 环境，且不能再引入其他 Web 容器，而我们的 common 中统一引入了 Tomcat，因此需要排除：
```xml
<dependency>
    <groupId>com.guli.mall</groupId>
    <artifactId>mall-common</artifactId>
    <version>1.0-SNAPSHOT</version>
    <exclusions>
    <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

我们编写 bootstrap.yml 文件，填入如下必要配置：
```yml
spring:
  application:
    name: mall-gateway
  cloud:
    nacos:
      config:
        server-addr: localhost:8848
        file-extension: yml
```

之后，我们创建并编写启动类，测试能否启动，需要注意网关也要作为 nacos 服务注册到注册中心去以发现其他业务微服务并做后续的转发：
```java
@SpringBootApplication
@EnableDiscoveryClient
public class MallGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallGatewayApplication.class, args);
    }
}
```

我们启动时会发现报没有数据源配置的错误，这是因为 common 统一引入了数据源启动器但我们没有做相应的配置。由于我们的网关服务不需要连接数据库，因此我们可以将数据源相关配置排除出去，此处有两种排除方案：
1. 直接排除依赖：直接在 common 的 exclusion 中排除掉 mybatis 和数据库驱动的依赖
```xml
<dependency>
    <groupId>com.guli.mall</groupId>
    <artifactId>mall-common</artifactId>
    <version>1.0-SNAPSHOT</version>
    <exclusions>
    <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </exclusion>
    <exclusion>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
    </exclusion>
    <exclusion>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </exclusion>
    </exclusions>
</dependency>
```
2. 也可以利用 SpringBootApplication 的 exclude 属性，排除掉 DataSourceAutoConfiguration 配置类 `@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})`

排除完毕后，即可正常启动，我们在 nacos 中创建 mall-gateway.yml 配置文件，先暂时填入下述配置并启动：
```yml
server:
  port: 88
```

# 网关重定向 demo 测试

我们以这样一个需求来学习并测试 Spring Cloud Gateway 的使用：假设我们访问 localhost:88?url=qq，则重定向到 www.qq.com，如果我们访问 localhost:88?url=baidu，则重定向到 www.baidu.com，基于这样一个需求我们来编写 Spring Cloud Gateway 的路由配置。

我们参照[官方文档](https://docs.spring.io/spring-cloud-gateway/docs/2.2.9.RELEASE/reference/html/)编写配置，由于我们需要根据查询参数的值进行重定向，因此浏览官方文档，符合条件的路由断言工厂为 Query 路由断言工厂，故我们在 mall-gateway.yml 中编写如下配置：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: qq_route
          uri: https://www.qq.com
          predicates:
            - Query=url, qq
        - id: baidu_route
          uri: https://www.baidu.com
          predicates:
            - Query=url, baidu
```

我们访问地址 `http://localhost:88/s?url=baidu&&wd=你好` 测试是否可以完成搜索，同时访问 `http://localhost:88?url=qq` 测试是否可以访问腾讯，可以则说明配置成功。 