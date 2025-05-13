# 项目说明

本项目是一个基于 Spring Boot 和 Spring Cloud 的分布式应用框架。

## 模块结构

- `apps-parent`: Maven 父模块，管理所有子模块的依赖和插件版本。
- `app1`, `app2`, `app3`: 子应用模块，每个子应用都包含以下三层结构：
    - `xxx-start`: 应用启动和配置模块。
    - `xxx-web`: 应用的视图和控制层模块。
    - `xxx-service`: 应用的业务逻辑和数据访问模块。

## 技术栈

- Spring Boot: 3.2.3
- Spring Cloud: 2023.0.0
- Hutool: 5.8.25
- Fastjson2: 2.0.47
- Java: 17

## 编码规范

本章节详细说明了项目中各个模块的职责和建议包含的组件。

### 1. xxx-start 模块（启动入口模块）

这个模块主要负责应用的启动和核心配置。

**主要组件:**

- **启动类与配置:**
    - 主启动类 (`XxxApplication.java`): Spring Boot 应用的入口。
    - 全局配置类 (`GlobalConfig.java`): 应用级别的通用配置。
    - 定时任务配置 (`SchedulerConfig.java`): Quartz 或 Spring Task 相关配置。
    - 数据源配置 (`DataSourceConfig.java`): 数据库连接池等配置。
    - Web安全配置 (`SecurityConfig.java`): Spring Security 相关配置（如果需要）。
- **资源文件:**
    - `application.yml`: 应用主配置文件（端口、应用名、日志、环境特定配置等）。
    - `bootstrap.yml`: (可选) 如果使用 Spring Cloud Config 等配置中心，用于引导配置。

### 2. xxx-web 模块（视图控制层）

这个模块负责处理 HTTP 请求、响应客户端、以及与用户界面相关的功能。

**主要组件:**

- **Controller 层:**
    - `@RestController` 类: 处理 HTTP 请求，提供 RESTful API 接口。
    - 统一的接口文档配置: 例如集成 Swagger/OpenAPI 来自动生成API文档。
    - 统一的返回对象封装 (`Response.java`): 定义标准的API响应结构。
- **DTO (Data Transfer Object) 对象:**
    - 请求 DTO (`XxxRequest.java`): 封装客户端请求参数。
    - 响应 DTO (`XxxResponse.java`): 封装服务端响应数据。
    - 参数校验: 使用 JSR 303/380 (如 `@Valid`, `@NotNull`) 进行输入验证。
- **异常处理:**
    - 全局异常处理器 (`GlobalExceptionHandler.java`): 使用 `@ControllerAdvice` 统一处理应用中的异常。
    - 自定义异常类 (`BusinessException.java`, `ResourceNotFoundException.java` 等): 定义业务相关的特定异常。
- **拦截器 (Interceptors) / 过滤器 (Filters):**
    - 登录拦截器 (`LoginInterceptor.java`): 处理用户认证。
    - 权限校验拦截器 (`AuthInterceptor.java`): 处理用户授权。
    - 请求日志过滤器 (`RequestLoggingFilter.java`): 记录请求和响应信息。

### 3. xxx-service 模块（业务逻辑层）

这个模块包含核心业务逻辑、数据处理以及与外部服务的集成。

**主要组件:**

- **服务层 (Service Layer):**
    - 业务服务接口 (`XxxService.java`): 定义业务操作。
    - 业务服务实现 (`XxxServiceImpl.java`): 实现业务逻辑。
    - 领域模型 (Domain Objects): 代表业务核心概念的类。
- **数据访问层 (Data Access Layer):**
    - 实体类 (Entity): JPA/Hibernate 实体或 MyBatis 的 POJO。
    - 数据访问接口 (Repository/Mapper): 例如 Spring Data JPA Repository 或 MyBatis Mapper 接口。
    - MyBatis 映射文件 (`XxxMapper.xml`): (如果使用 MyBatis) SQL 语句的 XML 配置文件。
- **RPC 相关:**
    - RPC 服务接口定义 (`XxxRpcService.java`): (如果使用 Dubbo/gRPC 等) 定义对外暴露的 RPC 服务。
    - RPC 服务实现 (`XxxRpcServiceImpl.java`): RPC 服务的具体实现。
    - RPC 客户端接口/代理 (`XxxClient.java`): 用于调用其他服务的 RPC 客户端。
- **定时任务 (Scheduled Tasks):**
    - 任务执行类 (`XxxJob.java`): 包含具体的定时任务逻辑。
    - 任务调度逻辑 (`XxxScheduler.java`): 配置和管理定时任务的执行。
- **工具类 (Utilities):**
    - 常量类 (`Constants.java`): 定义项目中使用的常量。
    - 通用工具类 (`Utils.java`): 提供可复用的辅助方法。
    - 转换器 (`Converter.java`): 用于不同对象（如 DTO 和 Entity）之间的转换。

### 模块依赖关系建议:

- `xxx-start` 模块依赖 `xxx-web` 和 `xxx-service` 模块。
- `xxx-web` 模块依赖 `xxx-service` 模块。
- `xxx-service` 模块应尽量保持独立，不反向依赖 `xxx-web` 或 `xxx-start` 模块。

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
每个应用的 `xxx-start` 模块包含一个主启动类 `XxxApplication`。可以直接在 IDE 中运行该类，或者将应用打包成 JAR 文件后通过以下命令运行：
```bash
java -jar appN/appN-start/target/appN-start-1.0-SNAPSHOT.jar
```
(将 `appN` 替换为 `app1`, `app2`, 或 `app3`)

配置端口：
- app1: 8081
- app2: 8082
- app3: 8083 