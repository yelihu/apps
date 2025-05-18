# 在macOS上安装单机版EK(Elasticsearch和Kibana)环境技术指南

## 1. 环境要求

- macOS版本：macOS Ventura或更高版本
- JDK：最低11版本（Elasticsearch 8.18.1自带了JDK）
- 硬盘空间：至少2GB可用空间
- 内存：至少2GB可用内存

## 2. 安装步骤

### 2.1 下载安装包

从Elastic官网下载适合macOS的Elasticsearch和Kibana安装包：

```bash
# 创建安装目录
mkdir -p ~/dev/es
cd ~/dev/es

# 下载Elasticsearch
curl -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-8.18.1-darwin-aarch64.tar.gz

# 下载Kibana
curl -O https://artifacts.elastic.co/downloads/kibana/kibana-8.18.1-darwin-aarch64.tar.gz
```

### 2.2 解压安装包

```bash
# 解压Elasticsearch
tar -xzf elasticsearch-8.18.1-darwin-aarch64.tar.gz

# 解压Kibana
tar -xzf kibana-8.18.1-darwin-aarch64.tar.gz
```

### 2.3 解决macOS安全策略问题

新版本的macOS（**MacOS Sonoma 版本14.3.1**）的安全策略会阻止运行未签名的应用，需要移除隔离标记：

```bash
# 移除Elasticsearch隔离标记
xattr -dr com.apple.quarantine ~/dev/es/elasticsearch-8.18.1/jdk.app

# 移除Kibana隔离标记
xattr -dr com.apple.quarantine ~/dev/es/kibana-8.18.1/node
```

## 3. 配置服务

### 3.1 配置Elasticsearch

编辑`~/dev/es/elasticsearch-8.18.1/config/elasticsearch.yml`：

```yaml
# 单节点模式配置
discovery.type: single-node

# 基础网络设置
network.host: 0.0.0.0
http.port: 9200

# 关闭安全设置（仅开发环境）
xpack.security.enabled: false
```

编辑`~/dev/es/elasticsearch-8.18.1/config/jvm.options`：

```
# 设置合适的内存大小
-Xms512m
-Xmx512m
```

### 3.2 配置Kibana

编辑`~/dev/es/kibana-8.18.1/config/kibana.yml`：

```yaml
# 服务器设置
server.port: 5601
server.host: "0.0.0.0"

# Elasticsearch连接设置
elasticsearch.hosts: ["http://localhost:9200"]

# 中文界面设置
i18n.locale: "zh-CN"
```

## 4. 创建启动脚本

创建`~/dev/es/start-elk.sh`文件：

```bash
#!/bin/bash

# ======= 配置项 =======
ES_HOME="/Users/用户名/dev/es/elasticsearch-8.18.1"
KIBANA_HOME="/Users/用户名/dev/es/kibana-8.18.1"
ES_MEMORY="512m"
ES_LOG_DIR="${ES_HOME}/logs"
KIBANA_LOG_DIR="${KIBANA_HOME}/logs"

# ======= 创建必要目录 =======
mkdir -p "$ES_LOG_DIR"
mkdir -p "$KIBANA_LOG_DIR"
mkdir -p "$ES_HOME/data"

# ======= 停止已有进程 =======
sudo pkill -f elasticsearch 2>/dev/null
sudo pkill -f kibana 2>/dev/null
sleep 2

# ======= 启动Elasticsearch =======
cd "$ES_HOME"
xattr -dr com.apple.quarantine "${ES_HOME}/jdk.app" 2>/dev/null || true
ES_JAVA_OPTS="-Xms${ES_MEMORY} -Xmx${ES_MEMORY}" \
  nohup "${ES_HOME}/bin/elasticsearch" > "${ES_LOG_DIR}/startup.log" 2>&1 &

# ======= 启动Kibana =======
cd "$KIBANA_HOME"
xattr -dr com.apple.quarantine "${KIBANA_HOME}/node" 2>/dev/null || true
nohup "${KIBANA_HOME}/bin/kibana" > "${KIBANA_LOG_DIR}/startup.log" 2>&1 &
```

赋予执行权限：

```bash
chmod +x ~/dev/es/start-elk.sh
```

## 5. 处理数据目录权限

确保数据目录有正确的权限：

```bash
# 如果遇到权限问题，可能需要清理数据目录并重新设置权限
sudo rm -rf ~/dev/es/elasticsearch-8.18.1/data
sudo mkdir -p ~/dev/es/elasticsearch-8.18.1/data
sudo chown -R $(whoami) ~/dev/es/elasticsearch-8.18.1/data
chmod -R 755 ~/dev/es/elasticsearch-8.18.1/data
```

## 6. 启动服务

```bash
cd ~/dev/es
./start-elk.sh
```

## 7. 验证服务

- Elasticsearch: http://localhost:9200
- Kibana: http://localhost:5601

## 8. 停止服务

```bash
sudo pkill -f elasticsearch && sudo pkill -f kibana
```

## 常见问题

1. **启动失败**：检查日志文件 `~/dev/es/elasticsearch-8.18.1/logs/startup.log`
2. **权限问题**：确保数据目录权限正确
3. **内存不足**：减小`jvm.options`中的内存设置
4. **端口冲突**：检查9200和5601端口是否被占用

## 注意事项

- 此配置仅适用于开发环境，生产环境应启用安全功能
- 首次启动可能需要几分钟时间
- 日志文件会记录详细的启动过程，可用于排查问题