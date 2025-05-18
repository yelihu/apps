# 分词器安装总结

## 介绍

IK分词器是目前最流行的中文分词器之一，支持细粒度和粗粒度两种分词模式，非常适合中文搜索应用。当您运行start-elk.sh脚本时，系统已能成功识别并加载此插件。





## 安装顺序

### 下载和基本安装

成功完成了IK分词器的手动安装过程：

1. 从官方源下载了与Elasticsearch 8.18.1兼容的IK分词器
   - 下载地址：https://release.infinilabs.com/analysis-ik/stable/
   - 下载文件：elasticsearch-analysis-ik-8.18.1.zip

2. 将下载的分词器安装到Elasticsearch插件目录
   - 路径：/Users/yelihu/dev/es/elasticsearch-8.18.1/plugins
   - 将插件重命名为"ik"

   

### 启动脚本处理

启动脚本`start-elk.sh`中关于插件的处理逻辑如下：

```bash
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
```

启动ES成功后，脚本还会显示已安装的插件列表：

```bash
    # 显示已安装的插件列表
    echo -e "\n${YELLOW}已安装的插件列表:${NC}"
    "${ES_HOME}/bin/elasticsearch-plugin" list || echo -e "${RED}无法获取插件列表${NC}"
```

这段代码的处理逻辑是：
1. 首先检查插件目录是否存在
2. 如果存在，计算插件数量并显示插件目录内容
3. 遍历每个插件目录，显示详细信息：
   - 从描述文件中提取版本、ES兼容版本、Java版本和描述
4. ES启动成功后，使用官方命令`elasticsearch-plugin list`显示已安装的插件列表

这样设计可以在启动前就知道插件的安装状态，同时在ES启动成功后通过官方工具再次确认插件的加载情况。