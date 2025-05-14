# 开发日志

## 2025-05-14 - 前端项目初始化与渐进式开发规划

1. **前端开发规划文档创建**：创建了 `doc/frontend_development_plan.md` 文档。
2. **前端项目初始化**：创建了 `apps-frontend` 目录，使用 React 18 + TypeScript + Ant Design 5 + Vite。
3. **基础组件开发**：创建了 `App.tsx`，添加了样式文件 `App.css` 和 `index.css`。
4. **本地开发环境配置**：安装了项目依赖，启动了开发服务器。
5. **后端项目调整**：添加了数据库、MyBatis Plus配置。
6. **文档工作**：新增 README.md，添加了 .gitignore 配置。
> 下一步：按照前端开发规划文档中的阶段二目标，开始引入路由和多页面结构，并实现与后端多个服务的集成。

## 2025-05-13 - 项目初始化与基础配置

1. 使用 spring-initializr 创建了三个应用 `app1`, `app2`, `app3`。
2. 创建了 Maven 父模块 `apps-parent` 及三个子应用 `app1`, `app2`, `app3`。
3. 在父 POM 中统一管理了核心依赖，并设置 Java 版本为 17。
4. 将配置文件转换为 `yml` 格式，配置了端口、应用名及 Actuator。
5. 初始化了 Git 仓库，创建了 `.gitignore` 文件，并完善了 `README.md`。
6. 添加了 `logback-spring.xml`，配置了日志输出。
7. 成功执行了 Maven 命令安装了父 POM 及所有子模块。

> 下一步设计：app2和app3能够使用app1暴露出来的客户端，未来能够借助这个客户端的定义，执行RPC调用（选用[dubbo3](https://cn.dubbo.apache.org/zh-cn/?spm=5238cd80.47ee59c.0.0.96bdcd36IZXcRD)进行远程调用、[Nacos3](https://nacos.io/docs/v3.0/overview/?spm=5238cd80.47ee59c.0.0.96bdcd36IZXcRD)管理服务）
> 