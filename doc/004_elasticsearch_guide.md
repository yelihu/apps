# Elasticsearch 集成设计与使用指南

## 1. 整体架构

Elasticsearch已集成到项目中，用于提供高效的搜索功能。主要用于搜索人员和部门信息，支持关键字搜索、精确匹配和多条件组合查询。

### 1.1 目录结构

```
com.example.app1.elasticsearch/
├── config/                    # 配置相关
│   └── ElasticsearchConfig.java  # ES客户端配置
├── domain/                    # 领域模型
│   ├── BaseDocument.java      # 文档基类
│   ├── PersonDocument.java    # 人员文档
│   └── DepartmentDocument.java # 部门文档
├── repository/                # 仓库层(可选)
├── service/                   # 服务层
│   ├── ElasticsearchService.java   # 服务接口
│   └── impl/
│       ├── BaseElasticsearchServiceImpl.java # 服务基类
│       ├── PersonDocumentServiceImpl.java    # 人员服务
│       └── DepartmentDocumentServiceImpl.java # 部门服务
└── util/                      # 工具类
    └── ElasticsearchQueryBuilder.java # 查询构建器
```

### 1.2 核心组件

- **ElasticsearchConfig**: 配置ES客户端连接
- **BaseDocument**: 所有文档的基类，包含共有字段
- **ElasticsearchService**: 通用服务接口
- **BaseElasticsearchServiceImpl**: 服务基础实现
- **ElasticsearchQueryBuilder**: 简化查询构建

## 2. 文档模型设计

### 2.1 通用文档字段 (BaseDocument)

| 字段名      | 字段类型   | 说明               |
|------------|-----------|-------------------|
| id         | String    | 文档ID             |
| create_time | Date      | 创建时间           |
| update_time | Date      | 更新时间           |
| creator    | Keyword   | 创建人             |
| updater    | Keyword   | 更新人             |

### 2.2 人员文档 (PersonDocument)

| 字段名          | 字段类型   | 分析器        | 说明               |
|----------------|-----------|--------------|-------------------|
| name           | Text      | ik_max_word  | 姓名              |
| employee_id    | Keyword   | -            | 工号              |
| department_id  | Keyword   | -            | 部门ID            |
| department_name | Text      | ik_max_word  | 部门名称           |
| position       | Text      | ik_max_word  | 职位              |
| email          | Keyword   | -            | 邮箱              |
| mobile         | Keyword   | -            | 手机号            |

### 2.3 部门文档 (DepartmentDocument)

| 字段名        | 字段类型   | 分析器       | 说明               |
|--------------|-----------|-------------|-------------------|
| name         | Text      | ik_max_word | 部门名称           |
| code         | Keyword   | -           | 部门编码           |
| parent_id    | Keyword   | -           | 父部门ID          |
| level        | Integer   | -           | 部门级别           |
| description  | Text      | ik_max_word | 部门描述           |
| path         | Keyword   | -           | 部门路径           |

## 3. 服务接口设计

### 3.1 通用服务接口 (ElasticsearchService)

```java
public interface ElasticsearchService<T> {
    // 索引操作
    void createIndex();
    void deleteIndex();
    boolean indexExists();
    
    // 文档操作
    T save(T document);
    Iterable<T> saveAll(List<T> documents);
    Optional<T> findById(String id);
    void deleteById(String id);
    void deleteAll();
    
    // 查询操作
    List<T> findByField(String fieldName, Object value);
    List<T> findByFields(Map<String, Object> fieldValues);
    List<T> searchByKeyword(String keyword, String... fieldNames);
    Page<T> searchByKeyword(String keyword, Pageable pageable, String... fieldNames);
    SearchHits<T> search(Query query);
}
```

### 3.2 人员服务 (PersonDocumentServiceImpl)

提供针对人员的特定查询方法：

```java
public class PersonDocumentServiceImpl extends BaseElasticsearchServiceImpl<PersonDocument> {
    // 查询特定部门的所有人员
    public List<PersonDocument> findByDepartment(String departmentId);
    
    // 根据工号查询人员
    public Optional<PersonDocument> findByEmployeeId(String employeeId);
}
```

### 3.3 部门服务 (DepartmentDocumentServiceImpl)

提供针对部门的特定查询方法：

```java
public class DepartmentDocumentServiceImpl extends BaseElasticsearchServiceImpl<DepartmentDocument> {
    // 根据编码查询部门
    public Optional<DepartmentDocument> findByCode(String code);
    
    // 查询子部门
    public List<DepartmentDocument> findChildren(String parentId);
    
    // 根据级别查询部门
    public List<DepartmentDocument> findByLevel(Integer level);
}
```

## 4. 索引初始化

> 注意：原`ElasticsearchIndexInitializer`类已移除，此处记录其功能，项目初始化需手动创建索引。

索引初始化器是在应用启动时自动创建所需索引的组件，现已移除，需要手动调用服务方法创建索引。

### 4.1 手动创建索引

在需要创建索引的地方，注入相应的服务并调用其`createIndex()`方法：

```java
@Autowired
private PersonDocumentService personDocumentService;

@Autowired
private DepartmentDocumentService departmentDocumentService;

// 初始化索引
public void initializeIndices() {
    // 检查索引是否存在，不存在则创建
    if (!personDocumentService.indexExists()) {
        personDocumentService.createIndex();
        log.info("人员索引创建成功");
    }
    
    if (!departmentDocumentService.indexExists()) {
        departmentDocumentService.createIndex();
        log.info("部门索引创建成功");
    }
}
```

### 4.2 生产环境建议

在生产环境，推荐使用以下方式管理索引：

1. **部署脚本**: 在应用部署过程中通过脚本创建索引
2. **管理接口**: 提供管理API手动创建/更新索引
3. **Kibana**: 使用Kibana的索引管理功能
4. **运维工具**: 使用专门的ES运维工具

## 5. 使用示例

### 5.1 保存文档

```java
// 创建人员文档
PersonDocument person = PersonDocument.builder()
    .name("张三")
    .employeeId("EMP001")
    .departmentId("DEPT001")
    .departmentName("研发部")
    .position("软件工程师")
    .email("zhangsan@example.com")
    .mobile("13800138000")
    .build();
person.initCreate("system");

// 保存到ES
personDocumentService.save(person);
```

### 5.2 关键字搜索

```java
// 搜索包含"工程师"的人员
List<PersonDocument> engineers = personDocumentService.searchByKeyword("工程师");

// 分页搜索
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
Page<PersonDocument> result = personDocumentService.searchByKeyword("研发", pageable);
```

### 5.3 精确查询

```java
// 查询特定部门的所有人员
List<PersonDocument> departmentStaff = personDocumentService.findByDepartment("DEPT001");

// 根据工号查询
Optional<PersonDocument> employee = personDocumentService.findByEmployeeId("EMP001");
```

## 6. 配置说明

### 6.1 application.yml 配置

```yaml
# Elasticsearch配置
elasticsearch:
  uris: http://localhost:9200
  socket-timeout: 30s
  connection-timeout: 5s
  restclient:
    sniffer:
      interval: 5m
      delay-after-failure: 30s
```

### 6.2 连接池优化

对于高并发场景，建议调整以下参数：

```yaml
spring:
  elasticsearch:
    rest:
      max-conn-per-route: 10
      max-conn-total: 100
``` 