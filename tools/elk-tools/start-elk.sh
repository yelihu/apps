#!/bin/bash

# 导入配置
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${SCRIPT_DIR}/elk-config.sh"

# ======= 颜色定义 =======
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# ======= 创建日志目录 =======
mkdir -p "$ES_LOG_DIR"
mkdir -p "$KIBANA_LOG_DIR"

# ======= 确保没有旧进程运行 =======
echo -e "${YELLOW}正在检查并关闭已存在的进程...${NC}"
pkill -f elasticsearch 2>/dev/null || true
pkill -f kibana 2>/dev/null || true
sleep 2

# ======= 启动Elasticsearch =======
echo -e "${YELLOW}正在启动Elasticsearch...${NC}"
cd "$ES_HOME"

# 检查插件目录
echo -e "${YELLOW}检查插件目录...${NC}"
if [ -d "${ES_HOME}/plugins" ]; then
  PLUGIN_COUNT=$(ls -1 "${ES_HOME}/plugins" | wc -l | xargs)
  if [ "$PLUGIN_COUNT" -gt 0 ]; then
    echo -e "${GREEN}发现${PLUGIN_COUNT}个插件目录:${NC}"
    ls -la "${ES_HOME}/plugins"
    
    # 显示每个插件的详细信息
    echo -e "\n${YELLOW}插件详细信息:${NC}"
    for plugin_dir in "${ES_HOME}/plugins"/*; do
      if [ -d "$plugin_dir" ]; then
        plugin_name=$(basename "$plugin_dir")
        echo -e "${GREEN}插件: ${plugin_name}${NC}"
        
        # 检查插件描述文件
        if [ -f "${plugin_dir}/plugin-descriptor.properties" ]; then
          echo -e "  版本: $(grep "version=" "${plugin_dir}/plugin-descriptor.properties" | cut -d'=' -f2)"
          echo -e "  ES兼容版本: $(grep "elasticsearch.version=" "${plugin_dir}/plugin-descriptor.properties" | cut -d'=' -f2)"
          echo -e "  Java版本: $(grep "java.version=" "${plugin_dir}/plugin-descriptor.properties" | cut -d'=' -f2)"
          echo -e "  描述: $(grep "description=" "${plugin_dir}/plugin-descriptor.properties" | cut -d'=' -f2)"
        else
          echo -e "  ${YELLOW}无法读取插件描述文件${NC}"
        fi
        echo ""
      fi
    done
  else
    echo -e "${YELLOW}插件目录为空，未安装任何插件${NC}"
  fi
else
  echo -e "${YELLOW}未找到插件目录，将在启动后创建${NC}"
fi

# 移除可能的隔离标记
xattr -dr com.apple.quarantine "${ES_HOME}/jdk.app" 2>/dev/null || true

# 启动ES (使用nohup确保后台运行，并重定向输出)
ES_JAVA_OPTS="-Xms${ES_MEMORY} -Xmx${ES_MEMORY}" \
  nohup "${ES_HOME}/bin/elasticsearch" > "${ES_LOG_DIR}/startup.log" 2>&1 &

ES_PID=$!
echo -e "${GREEN}Elasticsearch启动中，PID: $ES_PID${NC}"
echo "日志路径: ${ES_LOG_DIR}/startup.log"

# 等待ES启动
echo -e "${YELLOW}等待Elasticsearch启动完成...${NC}"
MAX_TRIES=30
COUNT=0
while [ $COUNT -lt $MAX_TRIES ]; do
  COUNT=$((COUNT+1))
  echo -n "."
  
  # 检查ES是否接受连接
  if curl -s "http://localhost:${ES_PORT}" > /dev/null; then
    echo -e "\n${GREEN}Elasticsearch成功启动!${NC}"
    
    # 显示集群信息
    echo -e "${YELLOW}集群信息:${NC}"
    curl -s "http://localhost:${ES_PORT}" | grep -E "cluster_name|number"
    
    # 显示已安装的插件列表
    echo -e "\n${YELLOW}已安装的插件列表:${NC}"
    "${ES_HOME}/bin/elasticsearch-plugin" list || echo -e "${RED}无法获取插件列表${NC}"
    
    break
  fi
  
  # ES进程是否仍在运行
  if ! ps -p $ES_PID > /dev/null; then
    echo -e "\n${RED}Elasticsearch启动失败，请检查日志${NC}"
    exit 1
  fi
  
  sleep 2
done

if [ $COUNT -eq $MAX_TRIES ]; then
  echo -e "\n${RED}等待Elasticsearch超时，但进程仍在运行，请手动检查${NC}"
fi

# ======= 启动Kibana =======
echo -e "${YELLOW}正在启动Kibana...${NC}"
cd "$KIBANA_HOME"

# 移除可能的隔离标记
xattr -dr com.apple.quarantine "${KIBANA_HOME}/node" 2>/dev/null || true

# 启动Kibana
nohup "${KIBANA_HOME}/bin/kibana" > "${KIBANA_LOG_DIR}/startup.log" 2>&1 &

KIBANA_PID=$!
echo -e "${GREEN}Kibana启动中，PID: $KIBANA_PID${NC}"
echo "日志路径: ${KIBANA_LOG_DIR}/startup.log"
echo -e "${GREEN}Kibana启动后可访问: http://localhost:${KIBANA_PORT}${NC}"

# ======= 保存PID到文件 =======
echo "$ES_PID" > "$ES_PID_FILE"
echo "$KIBANA_PID" > "$KIBANA_PID_FILE"

echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}服务已启动，访问地址:${NC}"
echo -e "Elasticsearch: ${YELLOW}http://localhost:${ES_PORT}${NC}"
echo -e "Kibana:        ${YELLOW}http://localhost:${KIBANA_PORT}${NC}"
echo
echo -e "${GREEN}如需停止服务，请执行:${NC}"
echo -e "${YELLOW}${SCRIPT_DIR}/stop-elk.sh${NC}"
echo -e "${GREEN}=====================================${NC}" 