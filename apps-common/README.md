# 公共服务模块 (apps-common)

这个模块是一个可共享的服务模块，为其他应用提供公共功能。

## 功能特性

- 提供HelloWorld服务，可通过配置进行自定义
- 支持通过Spring Boot自动配置机制集成到其他应用中
- 可作为独立应用运行或作为依赖引入

## 如何使用

### 1. 作为依赖引入

在其他应用的`pom.xml`中添加依赖:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>apps-common</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置

在应用的`application.yml`中添加以下配置:

```yaml
common:
  service:
    hello-world-enabled: true  # 是否启用HelloWorld服务
    welcome-message: "自定义的欢迎消息"  # 自定义欢迎消息
```

### 3. 使用服务

可以通过注入`HelloWorldService`来使用服务:

```java
@Service
@RequiredArgsConstructor
public class YourService {
    private final HelloWorldService helloWorldService;
    
    public void doSomething() {
        String message = helloWorldService.getWelcomeMessage();
        // 使用消息...
    }
}
```

### 4. API访问

当作为独立应用运行或集成到具有Web功能的应用中时，可通过以下API访问:

- 获取通用欢迎消息: `GET /api/common/hello`
- 获取个性化问候: `GET /api/common/hello/{name}`

## 作为独立应用运行

可以直接运行此模块作为独立服务:

```bash
mvn spring-boot:run
```

默认端口为8090，可在`application.yml`中修改。
