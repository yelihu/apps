# Apps项目说明

本项目基于Spring Boot和分布式应用框架，通过三个后端工程和一个前端工程，模拟企业级分布式应用集群。

该项目旨在：

1. 在MacOS环境下，利用IDE快速启动多个应用实例，构建分布式应用环境；
2.
利用这些应用编写各种示例，涵盖PRC框架、MQ、Redis缓存等中间件的使用及其高级用法；实现实用的系统设计场景，如分布式锁、基于消息表的分布式事务以及秒杀系统的实现和压力测试；实现各类AI提示模式，如Reflection和ReAct；
3. 掌握关键技术点，并生成总结文档，便于后续学习和分享。


## 模块结构

- `apps-parent`: Maven 父模块，管理所有子模块的依赖和插件版本。
- `app1`, `app2`, `app3`: 子应用模块，每个子应用都包含以下三层结构：
    - `start`: 应用启动和配置模块。
    - `web`: 应用的视图和控制层模块。
    - `service`: 应用的业务逻辑和数据访问模块。
- `apps-frentend`:  项目主要的前端工程

## 技术栈

- Spring Boot 3
- Hutool
- Fastjson2
- Java: 17
- MySQL 8
- MyBatis-plus
- ...



## 编码规范

本章节概述各模块职责及推荐组件：

### 1. start 模块（启动入口模块）

- **启动类与配置:** 主启动类 (`XxxApplication.java`)，全局配置类 (`GlobalConfig.java`)，定时任务配置 (
  `SchedulerConfig.java`)，数据源配置 (`DataSourceConfig.java`)，Web安全配置 (`SecurityConfig.java`)。
- **资源文件:** `application.yml`，`bootstrap.yml`。

### 2. web 模块（视图控制层）

- **Controller 层:** `@RestController` 类，Swagger/OpenAPI 文档，统一的返回对象封装 (`Response.java`)。
- **DTO 对象:** 请求 DTO (`XxxRequest.java`)，响应 DTO (`XxxResponse.java`)，参数校验。
- **异常处理:** 全局异常处理器 (`GlobalExceptionHandler.java`)，自定义异常类 (`BusinessException.java`,
  `ResourceNotFoundException.java`)。
- **拦截器/过滤器:** 登录、权限校验拦截器...

### 3. service 模块（业务逻辑层）

- **服务层:** 业务服务接口 (`XxxService.java`)，业务服务实现 (`XxxServiceImpl.java`)，领域模型。
- **数据访问层:** 实体类 (JPA/Hibernate)，数据访问接口 (Repository/Mapper)，MyBatis 映射文件 (`XxxMapper.xml`)。
- **RPC 相关:** RPC 服务接口定义 (`XxxRpcService.java`)，RPC 服务实现 (`XxxRpcServiceImpl.java`)，RPC 客户端接口 (
  `XxxClient.java`)。
- **定时任务:** 任务执行类 (`XxxJob.java`)，任务调度逻辑 (`XxxScheduler.java`)。
- **工具类:** 常量类 (`Constants.java`)，通用工具类 (`Utils.java`)，转换器 (`Converter.java`)。

### 模块依赖关系建议:
- `start` 模块依赖 `web` 和 `service` 模块。
- `web` 模块依赖 `service` 模块。
- `service` 模块应尽量保持独立，不反向依赖 `web` 或 `start` 模块。

### 包命名规范建议:

- 按照功能模块划分包结构，例如 `com.example.xxx.order`, `com.example.xxx.user`。
- 保持包名简洁且能清晰表达其含义。

### 代码复用建议:

- 考虑创建一个 `common` 或 `shared` 模块，用于存放多个应用或模块间可复用的组件，如工具类、通用的 DTO、枚举、自定义注解等。
- 将常用的工具类和基础设施相关的代码（如统一异常处理、统一响应封装的基类）放在 `common` 模块中。

### 接口设计建议:

- 遵循 RESTful API 设计规范。
- 确保接口具有良好的幂等性、安全性和可缓存性。
- 统一接口返回格式，包含状态码、消息和数据。
- 提供清晰、完整的接口文档。

## 构建和运行

### 前提条件
- JDK 17
- Maven 3.6+

### 构建步骤
1. 安装父 POM:
   ```bash
   mvn clean install -N
   ```
2. 安装所有子模块:
   ```bash
   mvn clean install
   ```

### 运行应用
每个应用的 `start` 模块包含一个主启动类 `XxxApplication`。可以直接在 IDE 中运行该类，或者将应用打包成 JAR 文件后通过以下命令运行：
```bash
java -jar appN/appN-start/target/appN-start-1.0-SNAPSHOT.jar
```
(将 `appN` 替换为 `app1`, `app2`, 或 `app3`)

配置端口：
- app1: 8081
- app2: 8082
- app3: 8083 