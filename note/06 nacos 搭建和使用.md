

# nacos 简介



[nacos](https://nacos.io/zh-cn/docs/quick-start.html) 是阿里巴巴开源的一个更易于构建云原生应用的动态服务发现、配置管理和服务管理
平台。 nacos 使用 java 编写，需要依赖 java 环境。在本项目中我们会使用 nacos 做为注册中心和配置中心。





# nacos 作为注册中心



## 业务微服务集成 nacos



参考[官方文档](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme-zh.md)，我们按如下步骤在我们的项目中引入 nacos。

- 首先，修改微服务的 pom.xml 文件，引入 Nacos Discovery Starter，为了避免重复编写，我们统一在 common 里引入。

  ```xml
  <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
  </dependency>
  ```

  

- 在应用的 /src/main/resources/application.properties 配置文件中配置 Nacos Server 地址。同时，需要配置业务微服务的名称，以便在注册到注册中心时进行识别，若不配置会失败

  ```yaml
  spring:
    application:
      name: mall-product
    cloud:
      nacos:
        discovery:
          server-addr: 127.0.0.1:8848
  ```

- 使用 @EnableDiscoveryClient 注解开启服务注册与发现功能（以 mall-product 为例）

  ```java
  @MapperScan("com.guli.mall.product.dao")
  @SpringBootApplication
  @EnableDiscoveryClient
  public class MallProductApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(MallProductApplication.class, args);
      }
  
  }
  ```

  


## nacos 服务器下载与启动

我们的各个微服务是以客户端的形式连接到 nacos 服务器的，因此我们还需要运行一个 nacos 服务器作为注册中心或配置中心。因此我们需要下载 [Nacos Server](https://github.com/alibaba/nacos/releases) 并在本地或者虚拟机启动 Nacos Server，并修改前文的连接地址 ` spring.cloud.nacos.discovery.server-add` 为真正运行的地址。



注意下载的 nacos server 版本要和我们引入的 nacos-client 版本一致，spring-cloud-starter-alibaba-nacos-discovery 会级联引入 nacos-client 依赖，可以在 IDEA 中观察到，此处我们 spring-cloud-starter-alibaba-nacos-discovery:2.1.4.RELEASE 依赖的版本是 nacos-client:1.4.1，因此我们下载的 nacos-server 版本也为 [1.4.1](https://github.com/alibaba/nacos/releases/tag/1.4.1)。



下载完毕并解压，之后再本地或虚拟机启动即可。nacos 有集群模式和单节点模式，我们在开发阶段先使用单节点模式启动 nacos 服务器，默认占用 8848 端口：

```bash
# linux
sh startup.sh -m standalone

# windows
startup.cmd -m standalone
```



启动成功后通过 localhost:8848/nacos 使用可视化界面 ，默认管理员账户密码为 nacos/nacos。



## 所有微服务注册到 nacos 并可视化验证



服务器启动成功后，修改前面 mall-product 微服务的 nacos  服务器地址，确保连接到 nacos 服务器，同时配置 spring.application.name，之后启动微服务。



访问 nacos 可视化界面，确保可以在服务管理的服务列表中看到相应的服务。



验证成功后，对所有业务微服务做相同操作并启动，并在控制台验证所有微服务都成功注册到 nacos。



## Feign 远程调用测试

- 参考 Spring Cloud OpenFeign 笔记



# nacos 作为配置中心



## 配置流程



根据[官方文档](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/readme-zh.md)，nacos 做为配置中心大致需要做如下配置，我们以 mall-coupon 为例进行介绍：

- 首先，为各个业务微服务引入 spring-cloud-starter-alibaba-nacos-config，我们统一在 common 模块中引入

  ```xml
  <!--nacos作为配置中心-->
  <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
  </dependency>
  ```

- 创建 bootstrap .yml 文件并配置 nacos 必备配置，同时指定采用 yaml 文件

  ```yaml
  spring:
    application:
      name: mall-coupon
    cloud:
      nacos:
        config:
          server-addr: 127.0.0.1:8848
          file-extension: yml
  ```

- 在 nacos 控制台的配置管理新建配置，创建 dataId 名为 mall-coupon.yml 的 yaml 格式配置：

  ```yaml
  spring:
    application:
      name: mall-coupon
    datasource:
      url: jdbc:mysql://192.168.25.51:3306/mall_sms
      username: root
      password: root
      driver-class-name: com.mysql.cj.jdbc.Driver
    cloud:
      nacos:
        discovery:
          server-addr: 127.0.0.1:8848
  mybatis-plus:
    mapper-locations: classpath:/mapper/**/*.xml # 只扫描自己工程的 mapper
    global-config:
      db-config:
        id-type: auto # 设置主键自增
  
  server:
    port: 7000
  ```

  

nacos-config-client 在连接 nacos 读取配置时，会根据 group 和 dataId 进行定位，我们在 nacos 中创建配置文件时也需要指定这两个的值 ，其中 group 的默认值为 DEFAULT_GROUP。

启动服务，验证服务成功启动，之后对所有微服务做同样更改，将配置移到配置中心中。



## nacos-client 寻找配置文件原则



nacos-config-client 寻找的 dataId 拼接格式为 `${prefix} - ${spring.profiles.active} . ${file-extension}`，如果 `prefix` 未配置则默认和 `spring.application.name` 一致，`spring.profiles.active` 未配置则无特殊环境，若配置了会读取公共配置和对应环境的配置，`file-extension` 未配置默认则为 properties，配置了则使用指定类型的配置文件。`group` 未配置则默认为 DEFAULT_GROUP。注意配置的 dataId 要代上后缀名，否则可能找不到。

故根据上述原则，我们的 mall-coupon 服务在连接 nacos 后读取的配置为 DEFAULT_GROUP 组下的 mall-coupon.yml。



我们使用下列步骤验证不带多环境的 dataId 寻找原则：

- 过添加一个 group=t_g，dataId = ada.yaml 配置文件进行
- 并修改端口为 7001
- 之后修改 bootstrap.yml 配置
- 重启服务，观察启动端口是否变化



我们使用下列步骤验证多环境寻找原则（使用 DEFAULT_GROUP）：

- 在 nacos 中创建 mall-coupon-dev.yaml，只设置端口为 7001
- 在 nacos 中创建 mall-coupon-test.yaml，只设置端口为 7002
- 在 nacos 中创建 mall-coupon-prod.yaml，只设置端口为 7003
- 修改  bootstrap.yml 文件，分别设置 `spring.profiles.active` 为不设置、dev、test、prod，验证服务分别在 7000, 7001, 7002, 7003 端口启动



## 动态获取配置

前面讲解得都是稳定配置项，变更后需要重启服务，接下来讲解如何基于 nacos 进行动态配置，其在运行期间变更配置后，无需重启，nacos-config-client 会自动读取最新的配置，我们仍然使用 mall-coupon 进行演示：

- 首先，在 mall-coupon.yml 中配置下述内容：

  ```yaml
  coupon:
    name: hao
    age: 18
  ```

  

