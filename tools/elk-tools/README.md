# ELK 工具集

这是一组用于管理Elasticsearch、Kibana服务的Shell脚本工具。

## 文件说明

- `elk-config.sh`: 集中配置文件，包含所有ELK相关的配置参数
- `start-elk.sh`: 启动Elasticsearch和Kibana服务的脚本
- `stop-elk.sh`: 停止Elasticsearch和Kibana服务的脚本

## 使用方法

### 启动ELK服务

```bash
./start-elk.sh
```

启动后可通过以下地址访问：
- Elasticsearch: http://localhost:9200
- Kibana: http://localhost:5601

### 停止ELK服务

```bash
./stop-elk.sh
```

## 配置说明

所有配置参数都集中在`elk-config.sh`文件中，您可以根据需要修改以下配置：

- `ES_HOME`: Elasticsearch安装目录
- `ES_MEMORY`: Elasticsearch内存配置
- `ES_PORT`: Elasticsearch端口
- `KIBANA_HOME`: Kibana安装目录
- `KIBANA_PORT`: Kibana端口

## 注意事项

1. 确保Elasticsearch和Kibana已正确安装在配置文件指定的目录中
2. 启动脚本会自动检查并关闭已存在的Elasticsearch和Kibana进程
3. 服务启动日志默认存放在对应安装目录的`logs`子目录中 