# 开发日志

## 2025-05-13 - 项目初始化与基础配置

1. **使用spring-initializr** 创建了三个应用 `app1`, `app2`, `app3`。使用的是[aliyun-spring-initializr](https://start.aliyun.com/)，选用MVC架构模式
2. **项目结构搭建**：创建了 Maven 父模块 `apps-parent` 及三个子应用 `app1`, `app2`, `app3`
2.  **依赖版本管理**：在父 POM 中统一管理了 Spring Boot/Cloud, Hutool, Fastjson2 等核心依赖，并设置 Java 版本为 17。
3.  **配置文件初始化与迁移**：将应用配置文件从 `properties` 转换为 `yml` 格式，集中到各应用的 `start` 模块，并配置了端口、应用名及 Actuator。
4.  **版本控制与文档**：初始化了 Git 仓库，创建了 `.gitignore` 文件，并创建和完善了项目 `README.md` 文件，包含项目说明、编码规范等。
5.  **日志系统配置 (Logback)**：为各应用的 `start` 模块添加了 `logback-spring.xml`，配置了控制台与文件日志输出（路径 `apps/logs/appN/appN.log`，按天滚动）及统一格式。
6.  **项目构建与安装**：成功执行了 Maven 命令安装了父 POM 及所有子模块。

> 下一步设计：app2和app3能够使用app1暴露出来的客户端，未来能够借助这个客户端的定义，执行RPC调用（选用[dubbo3](https://cn.dubbo.apache.org/zh-cn/?spm=5238cd80.47ee59c.0.0.96bdcd36IZXcRD)进行远程调用、[Nacos3](https://nacos.io/docs/v3.0/overview/?spm=5238cd80.47ee59c.0.0.96bdcd36IZXcRD)管理服务）
> 