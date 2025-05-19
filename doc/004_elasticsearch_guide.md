# Elasticsearch 在项目中的设计与实现文档

## 1. 整体架构

项目采用分层设计模式整合 Elasticsearch，主要包含以下组件：

- **领域模型层**：Document 类，映射 ES 索引
- **查询构建层**：查询 DSL 构建工具类
- **服务抽象层**：通用 ES 服务接口及抽象实现
- **业务服务层**：特定领域实体的 ES 操作服务

## 2. 核心组件

### 2.1 索引映射与文档模型

使用 `@Document` 注解标记的 POJO 类作为索引文档模型，例如：

```java
@Document(indexName = "person")
@Getter
@Setter
@Builder
public class PersonDocument {
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String name;
    
    @Field(type = FieldType.Keyword)
    private String nameSort;
    
    @Field(type = FieldType.Keyword)
    private String employeeId;
    
    // 其他字段...
}
```

特点：
- 区分全文搜索字段（Text）和精确匹配/排序字段（Keyword）
- 使用 IK 分词器增强中文搜索能力
- 针对同一属性提供不同索引方式（如 name/nameSort）

### 2.2 查询构建器

`ElasticsearchQueryBuilder` 工具类负责构建各类查询：

```java
public class ElasticsearchQueryBuilder {
    // 多字段匹配查询
    public static Query buildMultiMatchQuery(String keyword, String... fieldNames) {...}
    
    // 精确匹配查询
    public static Query buildTermQuery(String field, Object value) {...}
    
    // 范围查询
    public static Query buildRangeQuery(String field, Object from, Object to) {...}
    
    // 布尔查询
    public static BoolQueryBuilder buildBoolQuery() {...}
    
    // 其他查询类型...
}
```

特点：
- 封装复杂的 ES 查询 DSL
- 提供类型安全的查询构建方法
- 支持中文搜索的专用配置

### 2.3 基础服务层

抽象基类 `BaseElasticsearchServiceImpl` 实现通用操作：

```java
public abstract class BaseElasticsearchServiceImpl<T> implements ElasticsearchService<T> {
    // 索引管理
    public void createIndex() {...}
    public void deleteIndex() {...}
    public boolean indexExists() {...}
    
    // 文档操作
    public T save(T document) {...}
    public Iterable<T> saveAll(List<T> documents) {...}
    public Optional<T> findById(String id) {...}
    public void deleteById(String id) {...}
    
    // 查询操作
    public List<T> findByField(String fieldName, Object value) {...}
    public List<T> searchByKeyword(String keyword, String... fieldNames) {...}
    public Page<T> searchByKeyword(String keyword, Pageable pageable, String... fieldNames) {...}
    
    // 抽象方法
    protected abstract Query buildMultiMatchQuery(String keyword, String... fieldNames);
    protected abstract String getIndexName();
}
```

特点：
- 泛型设计，支持不同文档类型
- 面向接口编程，便于测试和扩展
- 集成索引管理与文档操作

### 2.4 业务服务层

特定领域实体服务类，如 `PersonDocumentServiceImpl`：

```java
@Service
public class PersonDocumentServiceImpl extends BaseElasticsearchServiceImpl<PersonDocument> {
    private static final String[] SEARCH_FIELDS = {"name", "department_name", "position"};
    
    // 覆盖父类方法，定制化查询
    @Override
    protected Query buildMultiMatchQuery(String keyword, String... fieldNames) {...}
    
    // 业务特定方法
    public List<PersonDocument> findByDepartment(String departmentId) {...}
    public Optional<PersonDocument> findByEmployeeId(String employeeId) {...}
    public List<PersonDocument> findByNamePrefix(String namePrefix) {...}
}
```

特点：
- 继承基础服务，专注业务逻辑
- 提供领域特定的查询方法
- 配置特定搜索字段和策略

## 3. 配置与初始化

### 3.1 核心配置

```properties
# application.properties
spring.elasticsearch.uris=http://localhost:9200
spring.elasticsearch.connection-timeout=1s
spring.elasticsearch.socket-timeout=30s
spring.elasticsearch.restclient.sniffer.interval=5m
```

### 3.2 测试配置

```java
@SpringBootTest
@ComponentScan(basePackages = {"com.example.app1", "com.example.common"})
@ActiveProfiles("test")
public class ElasticsearchServiceTest {
    // 测试初始化
    @BeforeEach
    public void setUp() {
        // 删除并重建索引
        if (personDocumentService.indexExists()) {
            personDocumentService.deleteIndex();
        }
        personDocumentService.createIndex();
        
        // 准备测试数据
        testPersons = Arrays.asList(...);
        personDocumentService.saveAll(testPersons);
        
        // 等待索引刷新
        Thread.sleep(3000);
    }
}
```

## 4. 最佳实践

1. **字段设计**
    - Text 类型用于全文搜索，配置适当分词器
    - Keyword 类型用于精确匹配、聚合和排序
    - 同一属性根据需要设置多种索引方式

2. **查询构建**
    - 使用工具类封装查询构建逻辑，提高复用性
    - 提供丰富的查询类型支持不同场景
    - 使用布尔查询组合多条件

3. **索引管理**
    - 封装索引创建、删除、刷新等操作
    - 提供索引存在性检查
    - 支持测试环境的索引重置

4. **性能优化**
    - 使用批量操作替代单条操作
    - 合理设置刷新间隔
    - 精确控制字段索引方式