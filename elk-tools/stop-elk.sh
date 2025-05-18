#!/bin/bash

# ======= 颜色定义 =======
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

# 导入配置
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${SCRIPT_DIR}/elk-config.sh"

echo -e "${YELLOW}正在停止ELK服务...${NC}"

# 读取PID文件 (如果存在)
ES_PID=""
KIBANA_PID=""

if [ -f "$ES_PID_FILE" ]; then
  ES_PID=$(cat "$ES_PID_FILE")
fi

if [ -f "$KIBANA_PID_FILE" ]; then
  KIBANA_PID=$(cat "$KIBANA_PID_FILE")
fi

# 停止Kibana
if [ -n "$KIBANA_PID" ] && ps -p $KIBANA_PID > /dev/null; then
  echo -e "${YELLOW}正在停止Kibana (PID: $KIBANA_PID)...${NC}"
  kill $KIBANA_PID 2>/dev/null
  sleep 2
  
  # 如果进程仍在运行，使用强制终止
  if ps -p $KIBANA_PID > /dev/null; then
    echo -e "${YELLOW}Kibana仍在运行，使用强制终止...${NC}"
    kill -9 $KIBANA_PID 2>/dev/null
  fi
fi

# 停止Elasticsearch
if [ -n "$ES_PID" ] && ps -p $ES_PID > /dev/null; then
  echo -e "${YELLOW}正在停止Elasticsearch (PID: $ES_PID)...${NC}"
  kill $ES_PID 2>/dev/null
  sleep 2
  
  # 如果进程仍在运行，使用强制终止
  if ps -p $ES_PID > /dev/null; then
    echo -e "${YELLOW}Elasticsearch仍在运行，使用强制终止...${NC}"
    kill -9 $ES_PID 2>/dev/null
  fi
fi

# 再次使用pkill确保所有进程被终止
echo -e "${YELLOW}确保所有相关进程已终止...${NC}"
pkill -f elasticsearch 2>/dev/null
pkill -f kibana 2>/dev/null

# 删除PID文件
rm -f "$ES_PID_FILE"
rm -f "$KIBANA_PID_FILE"

echo -e "${GREEN}所有ELK服务已停止${NC}" 