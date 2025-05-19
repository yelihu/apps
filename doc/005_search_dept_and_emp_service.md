
# 基于Spring Scheduler的MySQL-Elasticsearch同步与搜索方案

## 整体设计

你的思路很好，通过Spring Scheduler实现定时同步确实是一种简单有效的方案。我建议将架构设计为以下几个核心组件：

1. **定时同步组件**：使用@Scheduled注解实现
2. **数据访问层**：负责从MySQL读取数据
3. **ES操作组件**：负责写入和查询ES
4. **搜索服务**：实现统一的搜索接口

## 具体设计方案

### 1. 定时同步组件

```
@Component
@Slf4j
public class DataSyncScheduler {
    
    @Scheduled(cron = "0 0 1 * * ?")  // 每天凌晨1点执行
    public void syncDataToEs() {
        // 1. 从MySQL读取数据
        // 2. 删除ES中现有索引
        // 3. 重建索引并批量插入数据
        // 4. 记录同步结果日志
    }
}
```

### 2. 数据模型与索引设计

为简化设计，建议创建一个统一的"Subject"索引，包含两种类型的数据：

```
Subject {
    String id;           // 唯一标识，格式：EMP_{工号} 或 DEPT_{部门编号}
    String type;         // 类型：EMPLOYEE 或 DEPARTMENT
    String code;         // 工号或部门编号
    String name;         // 姓名或部门名称
    String position;     // 职位或null
    String managerName;  // 部门主管（部门才有）或null
    Date createDate;     // 入职日期或部门创建日期
    Map<String, Object> extraInfo;  // 其他可能需要搜索的字段
}
```

### 3. SubjectSearchService设计

```
@Service
public class SubjectSearchService {
    
    /**
     * 根据关键字搜索匹配的员工或部门信息
     * @param keyword 搜索关键词
     * @param topK 返回结果数量
     * @return 匹配的员工或部门列表
     */
    public List<Subject> search(String keyword, int topK) {
        // 1. 构建ES查询
        // 2. 执行搜索
        // 3. 处理结果并返回
    }
    
    /**
     * 按类型搜索
     */
    public List<Subject> searchByType(String keyword, String type, int topK) {
        // 增加类型过滤
    }
}
```

### 4. Elasticsearch查询设计

使用multi_match查询，对多个字段进行搜索：

```
{
  "size": topK,
  "query": {
    "bool": {
      "should": [
        {
          "multi_match": {
            "query": keyword,
            "fields": ["name^3", "code^2", "position", "managerName"],
            "type": "best_fields",
            "fuzziness": "AUTO"
          }
        }
      ]
    }
  },
  "highlight": {
    "fields": {
      "name": {},
      "position": {},
      "managerName": {}
    }
  }
}
```

### 5. 同步逻辑流程

1. 从MySQL读取员工表和部门表数据
2. 将数据转换为Subject对象
3. 删除ES中现有subject索引
4. 创建新索引并定义映射
5. 批量插入数据到ES

### 优势与考虑点

- **优势**：实现简单，易于维护
- **考虑点**：
  - 全量同步在数据量大时可能影响性能
  - 需要合理设置同步频率，避开业务高峰期
  - 可以考虑添加手动触发同步的接口

这个设计符合你的需求，通过定时任务简单地实现全量数据同步，并提供统一的搜索接口。后续可根据实际情况优化索引结构和查询策略。
