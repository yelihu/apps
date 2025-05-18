#!/bin/bash

# Elasticsearch配置
ES_HOME="/Users/yelihu/dev/es/elasticsearch-8.18.1"
ES_MEMORY="512m"
ES_PORT=9200

# Kibana配置
KIBANA_HOME="/Users/yelihu/dev/es/kibana-8.18.1"
KIBANA_PORT=5601

# 日志目录
ES_LOG_DIR="${ES_HOME}/logs"
KIBANA_LOG_DIR="${KIBANA_HOME}/logs"

# PID文件位置
PID_DIR="/tmp"
ES_PID_FILE="${PID_DIR}/elasticsearch.pid"
KIBANA_PID_FILE="${PID_DIR}/kibana.pid"

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color 