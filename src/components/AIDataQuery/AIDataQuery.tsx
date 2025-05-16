import React, { useState } from 'react';
import { Layout, Input, Button, List, Avatar, Typography, Table, Card, Spin } from 'antd';
import { SendOutlined, UserOutlined, RobotOutlined } from '@ant-design/icons';
import ReactMarkdown from 'react-markdown';
import './AIDataQuery.css';

const { Header, Content, Sider } = Layout;
const { TextArea } = Input;
const { Title, Text } = Typography;

// 定义聊天消息类型
interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
}

// 定义表格数据类型
interface TableData {
  columns: {
    title: string;
    dataIndex: string;
    key: string;
  }[];
  dataSource: Record<string, any>[];
}

// 结果类型
type ResultType = 'table' | 'markdown' | 'none';

// 查询结果
interface QueryResult {
  type: ResultType;
  data: TableData | string;
  title?: string;
}

// 生成唯一ID
const generateId = () => Math.random().toString(36).substring(2, 11);

const AIDataQuery: React.FC = () => {
  // 状态管理
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: generateId(),
      role: 'assistant',
      content: '你好！我是AI数据查询助手。你可以向我询问任何数据相关的问题，例如"查询最近7天的销售数据"或"分析上个月的用户增长趋势"。',
      timestamp: Date.now(),
    },
  ]);
  const [inputValue, setInputValue] = useState('');
  const [loading, setLoading] = useState(false);
  const [queryResult, setQueryResult] = useState<QueryResult>({
    type: 'none',
    data: '',
  });

  // 示例数据集
  const exampleTableData: TableData = {
    columns: [
      { title: '产品ID', dataIndex: 'productId', key: 'productId' },
      { title: '产品名称', dataIndex: 'name', key: 'name' },
      { title: '类别', dataIndex: 'category', key: 'category' },
      { title: '销售额', dataIndex: 'sales', key: 'sales' },
      { title: '销售日期', dataIndex: 'date', key: 'date' },
    ],
    dataSource: [
      { key: '1', productId: 'P001', name: '笔记本电脑', category: '电子产品', sales: 12500, date: '2023-05-01' },
      { key: '2', productId: 'P002', name: '智能手机', category: '电子产品', sales: 8700, date: '2023-05-02' },
      { key: '3', productId: 'P003', name: '无线耳机', category: '配件', sales: 3200, date: '2023-05-03' },
      { key: '4', productId: 'P004', name: '平板电脑', category: '电子产品', sales: 6800, date: '2023-05-04' },
      { key: '5', productId: 'P005', name: '智能手表', category: '配件', sales: 4500, date: '2023-05-05' },
    ],
  };

  const exampleMarkdownData = `
## 销售数据分析报告

过去7天的销售数据分析显示以下趋势：

1. **电子产品类**销售额占总销售额的 **72%**
2. **配件类**销售额占总销售额的 **28%**

### 主要发现

- 笔记本电脑是销售额最高的产品，达到 12,500 元
- 平均每日销售额为 7,140 元
- 销售额呈现上升趋势，周环比增长 15%

### 建议

- 加大电子产品类的营销投入
- 考虑对配件类产品进行促销活动
- 重点关注笔记本电脑和智能手机的库存管理
`;

  // 发送消息处理函数
  const handleSendMessage = () => {
    if (!inputValue.trim()) return;

    // 添加用户消息
    const userMessage: ChatMessage = {
      id: generateId(),
      role: 'user',
      content: inputValue,
      timestamp: Date.now(),
    };

    setMessages([...messages, userMessage]);
    setInputValue('');
    setLoading(true);

    // 模拟AI响应（实际项目中会调用后端API）
    setTimeout(() => {
      // 模拟AI回复
      const aiMessage: ChatMessage = {
        id: generateId(),
        role: 'assistant',
        content: '我已理解你的查询需求，正在生成结果...',
        timestamp: Date.now(),
      };
      setMessages(prev => [...prev, aiMessage]);

      // 模拟数据处理结果
      // 如果用户查询包含"销售"、"查询"等关键词，显示表格数据
      // 如果包含"分析"、"趋势"等关键词，显示Markdown分析
      if (userMessage.content.includes('销售') || userMessage.content.includes('查询')) {
        setTimeout(() => {
          setQueryResult({
            type: 'table',
            data: exampleTableData,
            title: '销售数据查询结果',
          });
          setLoading(false);
        }, 1000);
      } else if (userMessage.content.includes('分析') || userMessage.content.includes('趋势')) {
        setTimeout(() => {
          setQueryResult({
            type: 'markdown',
            data: exampleMarkdownData,
            title: '销售趋势分析报告',
          });
          setLoading(false);
        }, 1000);
      } else {
        setTimeout(() => {
          setMessages(prev => [
            ...prev.slice(0, -1),
            {
              ...aiMessage,
              content: '抱歉，我无法理解您的查询需求。请尝试使用更具体的查询词，例如"查询最近的销售数据"或"分析销售趋势"。',
            },
          ]);
          setLoading(false);
        }, 1000);
      }
    }, 1000);
  };

  // 渲染结果区域
  const renderResult = () => {
    if (loading) {
      return (
        <div className="result-loading">
          <Spin size="large" />
          <Text style={{ marginTop: 16 }}>正在处理您的查询...</Text>
        </div>
      );
    }

    switch (queryResult.type) {
      case 'table':
        const tableData = queryResult.data as TableData;
        return (
          <div className="result-content">
            <Title level={4}>{queryResult.title}</Title>
            <Table
              columns={tableData.columns}
              dataSource={tableData.dataSource}
              pagination={false}
              bordered
            />
          </div>
        );
      case 'markdown':
        return (
          <div className="result-content markdown-content">
            <Title level={4}>{queryResult.title}</Title>
            <ReactMarkdown>{queryResult.data as string}</ReactMarkdown>
          </div>
        );
      default:
        return (
          <div className="empty-result">
            <Text type="secondary">查询结果将显示在这里</Text>
          </div>
        );
    }
  };

  return (
    <Layout className="ai-data-query-container">
      {/* 左侧聊天区域 */}
      <Sider width={400} theme="light" className="chat-sider">
        <Header className="chat-header">
          <Title level={4}>AI 数据查询助手</Title>
        </Header>
        <Content className="chat-content">
          <List
            className="chat-list"
            itemLayout="horizontal"
            dataSource={messages}
            renderItem={(message) => (
              <List.Item className={`chat-item ${message.role}`}>
                <List.Item.Meta
                  avatar={
                    message.role === 'user' ? (
                      <Avatar icon={<UserOutlined />} />
                    ) : (
                      <Avatar icon={<RobotOutlined />} style={{ backgroundColor: '#1677ff' }} />
                    )
                  }
                  content={message.content}
                />
              </List.Item>
            )}
          />
        </Content>
        <div className="chat-input-container">
          <TextArea
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            placeholder="输入您的查询..."
            autoSize={{ minRows: 2, maxRows: 4 }}
            onPressEnter={(e) => {
              if (!e.shiftKey) {
                e.preventDefault();
                handleSendMessage();
              }
            }}
          />
          <Button
            type="primary"
            icon={<SendOutlined />}
            onClick={handleSendMessage}
            disabled={!inputValue.trim() || loading}
          >
            发送
          </Button>
        </div>
      </Sider>

      {/* 右侧结果展示区域 */}
      <Content className="result-container">
        <Card className="result-card" bordered={false}>
          {renderResult()}
        </Card>
      </Content>
    </Layout>
  );
};

export default AIDataQuery; 