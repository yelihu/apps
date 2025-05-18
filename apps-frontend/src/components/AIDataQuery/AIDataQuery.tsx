import React, { useState, useRef, useEffect } from 'react';
import {
  Layout, Input, Button, List, Avatar, Typography, Card, Spin,
  Divider, Tag, Alert, Empty, Space, Tooltip, Switch, Table, Dropdown
} from 'antd';
import type { ColumnsType } from 'antd/es/table';
import type { MenuProps } from 'antd';
import {
  SendOutlined, UserOutlined, RobotOutlined, 
  ReloadOutlined, SettingOutlined, InfoCircleOutlined, PlusCircleOutlined,
  BulbOutlined, BulbFilled, EyeOutlined, TrophyOutlined, FullscreenOutlined,
  FullscreenExitOutlined, MenuOutlined, HomeOutlined, AppstoreOutlined, 
  BarChartOutlined, LinkOutlined
} from '@ant-design/icons';
import axios from 'axios';
import './AIDataQuery.css';

const { Header, Content, Sider } = Layout;
const { Search } = Input;
const { Title, Text, Paragraph } = Typography;

// 定义聊天消息类型
interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
}

// 定义列表数据类型（修改为人员业绩数据）
interface ListData {
  header?: {
    title: string;
    subtitle?: string;
  };
  items: StaffPerformance[];
}

// 人员业绩数据接口
interface StaffPerformance {
  id: string;
  name: string;
  department: string;
  position: string;
  avatar?: string;
  performance: number;
  sales: number;
  clients: number;
  projects: number;
  ranking?: number;
  achievements?: string[];
  tags?: string[];
  [key: string]: any;
}

// 结果类型
type ResultType = 'list' | 'error' | 'none';

// 查询结果
interface QueryResult {
  id: string;
  type: ResultType;
  data: ListData | string;
  title?: string;
  timestamp: number;
  query?: string;
  url?: string;  // AI返回的URL
  isRefreshing?: boolean;
  originalType?: ResultType; // 错误情况下保存原始类型
}

// 生成唯一ID
const generateId = () => Math.random().toString(36).substring(2, 11);

const AIDataQuery: React.FC = () => {
  const [input, setInput] = useState<string>('');
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: '1',
      role: 'assistant',
      content: '你好！我是AI数据查询助手。我可以帮你查询数据或分析数据。请告诉我你想了解什么？',
      timestamp: Date.now(),
    },
  ]);
  const [loading, setLoading] = useState<boolean>(false);
  const [queryResults, setQueryResults] = useState<QueryResult[]>([]);
  const [darkMode, setDarkMode] = useState<boolean>(() => {
    const savedMode = localStorage.getItem('darkMode');
    return savedMode === 'true';
  });
  const [fullscreenResult, setFullscreenResult] = useState<string | null>(null);

  const chatContentRef = useRef<HTMLDivElement>(null);
  const resultContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (chatContentRef.current) {
      chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight;
    }
  }, [messages]);

  // 当状态发生变化时处理滚动
  useEffect(() => {
    if (resultContainerRef.current && (loading || queryResults.length > 0)) {
      // 当有加载或新结果时，滚动到底部
      resultContainerRef.current.scrollTop = resultContainerRef.current.scrollHeight;
    }
  }, [loading, queryResults]);

  const toggleDarkMode = () => {
    const newDarkMode = !darkMode;
    setDarkMode(newDarkMode);
    localStorage.setItem('darkMode', String(newDarkMode));
    if (newDarkMode) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  };

  useEffect(() => {
    if (darkMode) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  }, [darkMode]);

  const handleSendMessage = () => {
    if (!input.trim()) return;
    const userMessage: ChatMessage = {
      id: Date.now().toString(),
      role: 'user',
      content: input,
      timestamp: Date.now(),
    };
    const userQuery = input;
    setMessages((prev) => [...prev, userMessage]);
    setInput('');
    setLoading(true);
    setTimeout(() => {
      let mockUrl = '';
      let mockResultType: ResultType = 'none';
      if (userQuery.toLowerCase().includes('业绩') || userQuery.toLowerCase().includes('绩效') || userQuery.toLowerCase().includes('销售') || userQuery.toLowerCase().includes('团队')) {
        mockUrl = 'https://api.example.com/data/staff/performance?period=q2&dept=all';
        mockResultType = 'list';
        handleListResult(userQuery, mockUrl);
      } else {
        mockUrl = 'https://api.example.com/data/staff/overview?team=sales';
        mockResultType = 'list';
        handleListResult(userQuery, mockUrl);
      }
      setLoading(false);
    }, 1500);
  };

  const addTestMessages = () => {
    const testMessages: ChatMessage[] = [];
    for (let i = 1; i <= 10; i++) {
      testMessages.push({
        id: `test-user-${Date.now()}-${i}`,
        role: 'user',
        content: `这是测试消息 ${i}，用来测试聊天记录是否可以正常滚动`,
        timestamp: Date.now() + i
      });
      testMessages.push({
        id: `test-assistant-${Date.now()}-${i}`,
        role: 'assistant',
        content: `这是AI回复的测试消息 ${i}，测试聊天记录滚动功能和样式是否正常`,
        timestamp: Date.now() + i + 0.5
      });
    }
    setMessages(prev => [...prev, ...testMessages]);
  };

  const fetchDataFromUrl = async (url: string, resultType: ResultType, query: string) => {
    try {
      console.log('模拟从URL获取数据:', url, resultType);
      return true;
    } catch (error) {
      console.error('获取数据失败:', error);
      const errorResult: QueryResult = {
        id: Date.now().toString(),
        type: 'error',
        data: `获取数据失败: ${error instanceof Error ? error.message : String(error)}`,
        title: '查询错误',
        timestamp: Date.now(),
        query,
        url,
        originalType: resultType
      };
      setQueryResults(prev => [...prev, errorResult]);
      return false;
    }
  };

  const refreshResult = async (resultId: string) => {
    const targetResult = queryResults.find(result => result.id === resultId);
    if (!targetResult || !targetResult.url) return;
    setQueryResults(prev => prev.map(item =>
      item.id === resultId ? {...item, isRefreshing: true} : item
    ));
    setTimeout(() => {
      setQueryResults(prev => prev.map(item =>
        item.id === resultId ? {
          ...item,
          timestamp: Date.now(),
          isRefreshing: false
        } : item
      ));
    }, 1000);
  };

  const handleListResult = (query: string, url: string) => {
    const aiMessage: ChatMessage = {
      id: Date.now().toString(),
      role: 'assistant',
      content: `我已找到相关的人员业绩数据，请在右侧查看结果表格。\n\n查询URL: ${url}`,
      timestamp: Date.now(),
    };
    setMessages((prev) => [...prev, aiMessage]);
    
    let listData: ListData;
    if (query.toLowerCase().includes('销售') || query.toLowerCase().includes('业绩')) {
      listData = {
        header: { title: '销售团队业绩表现', subtitle: '2023年第二季度' },
        items: [
          { id: '1', name: '张三', department: '销售部', position: '高级销售经理', performance: 92, sales: 1250000, clients: 48, projects: 7, ranking: 1, achievements: ['季度之星', '最佳新客户开发'], tags: ['销售精英', '团队领导']},
          { id: '2', name: '李四', department: '销售部', position: '销售主管', performance: 86, sales: 980000, clients: 35, projects: 5, ranking: 3, achievements: ['客户满意度最高'], tags: ['客户关系', '解决方案销售']},
          { id: '3', name: '王五', department: '销售部', position: '区域销售经理', performance: 88, sales: 1020000, clients: 42, projects: 6, ranking: 2, achievements: ['区域业绩提升50%'], tags: ['区域开发', '团队协作']},
          { id: '4', name: '赵六', department: '销售部', position: '销售代表', performance: 78, sales: 760000, clients: 28, projects: 4, ranking: 5, achievements: ['新人成长最快'], tags: ['潜力新星', '产品专家']},
          { id: '5', name: '钱七', department: '销售部', position: '客户经理', performance: 84, sales: 880000, clients: 32, projects: 5, ranking: 4, achievements: ['老客户维护奖'], tags: ['客户维护', '增量销售']},
          { id: '6', name: '孙八', department: '销售部', position: '销售顾问', performance: 76, sales: 680000, clients: 24, projects: 3, ranking: 6, achievements: ['技术支持优秀'], tags: ['技术销售', '行业专家']},
          { id: '7', name: '周九', department: '销售部', position: '销售经理', performance: 83, sales: 920000, clients: 38, projects: 4, ranking: 7, achievements: ['最佳团队管理'], tags: ['团队协作', '解决方案销售']},
          { id: '8', name: '吴十', department: '销售部', position: '高级销售代表', performance: 81, sales: 830000, clients: 30, projects: 5, ranking: 8, achievements: ['产品知识专家'], tags: ['产品专家', '技术销售']},
          { id: '9', name: '郑十一', department: '销售部', position: '销售代表', performance: 75, sales: 620000, clients: 22, projects: 3, ranking: 10, achievements: ['新人进步奖'], tags: ['新人', '潜力新星']},
          { id: '10', name: '马十二', department: '销售部', position: '客户经理', performance: 79, sales: 790000, clients: 26, projects: 4, ranking: 9, achievements: ['客户续约专家'], tags: ['客户维护', '关系管理']},
          { id: '11', name: '林十三', department: '销售部', position: '销售主管', performance: 80, sales: 850000, clients: 29, projects: 5, ranking: 11, achievements: ['团队协作奖'], tags: ['团队领导', '销售策略']},
          { id: '12', name: '刘十四', department: '销售部', position: '销售代表', performance: 73, sales: 580000, clients: 20, projects: 2, ranking: 13, achievements: ['新人奖'], tags: ['新人', '产品专家']},
          { id: '13', name: '陈十五', department: '销售部', position: '区域销售代表', performance: 77, sales: 720000, clients: 25, projects: 3, ranking: 12, achievements: ['区域客户满意度第一'], tags: ['客户服务', '区域开发']},
          { id: '14', name: '黄十六', department: '销售部', position: '销售顾问', performance: 71, sales: 520000, clients: 18, projects: 2, ranking: 14, achievements: ['专业知识奖'], tags: ['产品专家', '技术支持']},
          { id: '15', name: '杨十七', department: '销售部', position: '销售实习生', performance: 68, sales: 380000, clients: 12, projects: 1, ranking: 15, achievements: ['最佳新人奖'], tags: ['新人', '潜力股']}
        ]
      };
    } else {
      listData = {
        header: { title: '研发团队业绩表现', subtitle: '2023年第二季度' },
        items: [
          { id: '1', name: '陈一', department: '研发部', position: '技术总监', performance: 95, sales: 420000, clients: 3, projects: 12, ranking: 1, achievements: ['核心系统架构设计', '技术创新奖'], tags: ['架构设计', '团队管理']},
          { id: '2', name: '林二', department: '研发部', position: '前端负责人', performance: 89, sales: 350000, clients: 2, projects: 8, ranking: 3, achievements: ['用户体验优化'], tags: ['前端开发', 'UI设计']},
          { id: '3', name: '黄三', department: '研发部', position: '后端工程师', performance: 90, sales: 380000, clients: 0, projects: 10, ranking: 2, achievements: ['系统性能提升30%'], tags: ['高性能', '数据库优化']},
          { id: '4', name: '周四', department: '研发部', position: '测试经理', performance: 87, sales: 280000, clients: 1, projects: 11, ranking: 4, achievements: ['测试流程改进'], tags: ['自动化测试', '质量把控']},
          { id: '5', name: '吴五', department: '研发部', position: '移动开发工程师', performance: 85, sales: 320000, clients: 0, projects: 7, ranking: 5, achievements: ['App用户增长40%'], tags: ['移动开发', '用户体验']},
          { id: '6', name: '郑六', department: '研发部', position: 'DevOps工程师', performance: 82, sales: 210000, clients: 0, projects: 9, ranking: 6, achievements: ['部署时间减少50%'], tags: ['自动化部署', '持续集成']},
          { id: '7', name: '张七', department: '研发部', position: '算法工程师', performance: 91, sales: 360000, clients: 0, projects: 6, ranking: 7, achievements: ['算法优化奖', '性能提升35%'], tags: ['算法优化', '数据分析']},
          { id: '8', name: '王八', department: '研发部', position: '安全工程师', performance: 88, sales: 290000, clients: 1, projects: 5, ranking: 8, achievements: ['安全审计最佳实践'], tags: ['安全架构', '漏洞检测']},
          { id: '9', name: '李九', department: '研发部', position: '数据库工程师', performance: 86, sales: 260000, clients: 0, projects: 8, ranking: 9, achievements: ['数据库性能优化'], tags: ['数据库', '性能调优']},
          { id: '10', name: '赵十', department: '研发部', position: 'UI设计师', performance: 83, sales: 240000, clients: 2, projects: 7, ranking: 10, achievements: ['最佳用户界面设计'], tags: ['UI设计', '用户体验']},
          { id: '11', name: '钱十一', department: '研发部', position: '产品经理', performance: 84, sales: 390000, clients: 3, projects: 6, ranking: 11, achievements: ['产品创新奖'], tags: ['产品设计', '用户研究']},
          { id: '12', name: '孙十二', department: '研发部', position: '前端工程师', performance: 81, sales: 230000, clients: 0, projects: 5, ranking: 12, achievements: ['前端性能优化'], tags: ['前端开发', '性能优化']},
          { id: '13', name: '周十三', department: '研发部', position: '后端工程师', performance: 82, sales: 220000, clients: 0, projects: 7, ranking: 13, achievements: ['代码质量提升'], tags: ['后端开发', '代码重构']},
          { id: '14', name: '吴十四', department: '研发部', position: '测试工程师', performance: 79, sales: 180000, clients: 0, projects: 8, ranking: 14, achievements: ['自动化测试覆盖率提升'], tags: ['自动化测试', '质量保障']},
          { id: '15', name: '郑十五', department: '研发部', position: '实习工程师', performance: 76, sales: 120000, clients: 0, projects: 4, ranking: 15, achievements: ['最佳新人奖'], tags: ['新人', '潜力新星']}
        ]
      };
    }
    const newResult: QueryResult = {
      id: Date.now().toString(), type: 'list', data: listData,
      title: listData.header?.title || '人员业绩数据', timestamp: Date.now(), query: query, url: url
    };
    setQueryResults(prev => [...prev, newResult]);
  };

  const formatTime = (timestamp: number) => {
    const date = new Date(timestamp);
    return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
  };

  // 定义表格列配置
  const getTableColumns = (): ColumnsType<StaffPerformance> => {
    return [
      {
        title: '员工',
        dataIndex: 'name',
        key: 'name',
        render: (text, record) => (
          <div className="staff-info-cell">
            <div className="staff-details">
              <div className="staff-name">{text}</div>
              <div className="staff-position">{record.position}</div>
            </div>
          </div>
        ),
        width: 160,
      },
      {
        title: '部门',
        dataIndex: 'department',
        key: 'department',
        width: 100,
      },
      {
        title: '绩效',
        dataIndex: 'performance',
        key: 'performance',
        sorter: {
          compare: (a, b) => a.performance - b.performance,
          multiple: 4
        },
        render: (value) => (
          <div className="performance-cell">
            <span className={`performance-value ${value >= 90 ? 'excellent' : value >= 80 ? 'good' : value >= 70 ? 'average' : 'poor'}`}>
              {value}
            </span>
          </div>
        ),
        width: 100,
      },
      {
        title: '销售额',
        dataIndex: 'sales',
        key: 'sales',
        sorter: {
          compare: (a, b) => a.sales - b.sales,
          multiple: 3
        },
        render: (value) => value !== undefined && value !== null ? `¥${(value/10000).toFixed(1)}万` : '-',
        width: 100,
      },
      {
        title: '客户数',
        dataIndex: 'clients',
        key: 'clients',
        sorter: {
          compare: (a, b) => a.clients - b.clients,
          multiple: 2
        },
        width: 90,
      },
      {
        title: '项目数',
        dataIndex: 'projects',
        key: 'projects',
        sorter: {
          compare: (a, b) => a.projects - b.projects,
          multiple: 1
        },
        width: 90,
      },
      {
        title: '排名',
        dataIndex: 'ranking',
        key: 'ranking',
        sorter: {
          compare: (a, b) => (a.ranking || 999) - (b.ranking || 999),
          multiple: 0
        },
        render: (value) => value ? (
          <span className="ranking">
            {value <= 3 ? <TrophyOutlined className={`trophy rank-${value}`} /> : null}
            {value}
          </span>
        ) : '-',
        width: 80,
      },
      {
        title: '专长',
        dataIndex: 'tags',
        key: 'tags',
        render: (tags) => (
          <div className="tag-container">
            {tags && tags.map((tag: string, index: number) => (
              <Tag key={index} color={index % 2 === 0 ? 'blue' : 'green'}>
                {tag}
              </Tag>
            ))}
          </div>
        ),
      },
      {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        render: (_, record) => (
          <Space size="small">
            <Button type="link" size="small" icon={<EyeOutlined />}>详情</Button>
          </Space>
        ),
        width: 80,
      },
    ];
  };

  // 切换全屏显示
  const toggleFullscreen = (resultId: string) => {
    setFullscreenResult(fullscreenResult === resultId ? null : resultId);
  };

  const renderCardActions = (result: QueryResult) => {
    const actions = [];
    if (result.url) {
      actions.push(
        <Tooltip key="refresh" title="刷新数据">
          <Button type="text" icon={<ReloadOutlined />} onClick={() => refreshResult(result.id)} loading={result.isRefreshing} className="action-button" />
        </Tooltip>
      );
    }
    actions.push(
      <Tooltip key="fullscreen" title={fullscreenResult === result.id ? "退出全屏" : "全屏查看"}>
        <Button 
          type="text" 
          icon={fullscreenResult === result.id ? <FullscreenExitOutlined /> : <FullscreenOutlined />} 
          onClick={() => toggleFullscreen(result.id)} 
          className="action-button" 
        />
      </Tooltip>
    );
    actions.push(
      <Tooltip key="more" title="更多操作">
        <Button type="text" icon={<SettingOutlined />} className="action-button" />
      </Tooltip>
    );
    return <Space>{actions}</Space>;
  };

  // 菜单项配置
  const menuItems: MenuProps['items'] = [
    {
      key: '1',
      label: '首页',
      icon: <HomeOutlined />,
      onClick: () => console.log('点击了首页')
    },
    {
      key: '2',
      label: '应用中心',
      icon: <AppstoreOutlined />,
      onClick: () => console.log('点击了应用中心')
    },
    {
      key: '3',
      label: '数据报表',
      icon: <BarChartOutlined />,
      onClick: () => console.log('点击了数据报表')
    },
    {
      key: '4',
      label: '外部链接',
      icon: <LinkOutlined />,
      onClick: () => console.log('点击了外部链接')
    },
  ];

  return (
    <Layout className={`ai-data-query-container ${darkMode ? 'dark-theme' : ''}`}>
      <Sider width={400} theme={darkMode ? "dark" : "light"} className="chat-sider">
        <Header className="chat-header">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Dropdown menu={{ items: menuItems }} placement="bottomLeft" trigger={['click']}>
              <Button type="text" icon={<MenuOutlined />} className="menu-button" />
            </Dropdown>
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <Tooltip title={darkMode ? "切换到亮色模式" : "切换到暗黑模式"}>
                <Switch
                  checkedChildren={<BulbFilled />}
                  unCheckedChildren={<BulbOutlined />}
                  checked={darkMode}
                  onChange={toggleDarkMode}
                  style={{ marginRight: '12px' }}
                />
              </Tooltip>
              <Tooltip title="添加测试消息（仅用于测试滚动）">
                <Button icon={<PlusCircleOutlined />} type="text" onClick={addTestMessages} />
              </Tooltip>
            </div>
          </div>
        </Header>
        <div className="chat-content" ref={chatContentRef}>
          <List
            className="chat-list"
            dataSource={messages}
            renderItem={(item) => (
              <List.Item className={`chat-item ${item.role}`}>
                <div className="message-content">
                  {item.role === 'assistant' && (
                    <Avatar
                      className="avatar-icon robot-avatar"
                      style={{ backgroundColor: '#1677ff' }}
                    >
                      🤖
                    </Avatar>
                  )}
                  <div className="message-text">
                    {item.content}
                  </div>
                  {item.role === 'user' && (
                    <Avatar
                      className="avatar-icon user-avatar"
                      style={{ backgroundColor: '#fa8c16' }}
                    >
                      🥰
                    </Avatar>
                  )}
                </div>
              </List.Item>
            )}
          />
        </div>
        <div className="chat-input-container">
          <Search
            placeholder="请输入你的查询..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onSearch={handleSendMessage}
            enterButton={<SendOutlined />}
            size="large"
            loading={loading}
            className="chat-search-input"
          />
        </div>
      </Sider>
      <Content className={`result-container ${darkMode ? 'dark-theme' : ''}`} ref={resultContainerRef}>
        {queryResults.length === 0 && !loading ? (
          <div className="empty-result">
            <p>请在左侧输入您的查询，结果将显示在这里</p>
            <p className="tip-text">提示：尝试输入"查询团队业绩"、"销售部业绩排名"或"研发团队绩效"</p>
          </div>
        ) : (
          <>
            <div className="result-history">
              {queryResults.map((result, index) => {
                const listData = result.type === 'list' ? (result.data as ListData) : null;
                const isFullscreen = fullscreenResult === result.id;
                
                return (
                  <Card
                    key={result.id}
                    className={`result-card history-card ${isFullscreen ? 'fullscreen' : ''}`}
                    title={
                      <div className="result-card-title">
                        <div className="card-title-content">
                          <span>{result.title || '查询结果'}</span>
                          {listData && listData.header?.subtitle && (
                            <span className="card-subtitle">{listData.header.subtitle}</span>
                          )}
                        </div>
                        <span className="result-time">{formatTime(result.timestamp)}</span>
                      </div>
                    }
                    extra={
                      <div className="card-actions">
                        <Text type="secondary" style={{ marginRight: '16px' }}>
                          查询: {result.query}
                        </Text>
                        {renderCardActions(result)}
                      </div>
                    }
                    size="small"
                  >
                    <div className="result-content">
                      {result.type === 'error' && (
                        <Alert
                          message="查询错误"
                          description={result.data as string}
                          type="error"
                          showIcon
                          action={
                            <Button size="small" danger onClick={() => refreshResult(result.id)} loading={result.isRefreshing}>
                              重试
                            </Button>
                          }
                        />
                      )}
                      {result.type === 'list' && (
                        <div className="table-result">
                          <Table 
                            columns={getTableColumns()}
                            dataSource={(result.data as ListData).items.map(item => ({...item, key: item.id}))}
                            pagination={{ pageSize: 10, size: "small" }}
                            size="small"
                            className="data-table"
                            rowClassName={() => 'table-row'}
                            bordered
                            scroll={{ x: 'max-content' }}
                          />
                        </div>
                      )}
                    </div>
                    {index < queryResults.length - 1 && !isFullscreen && <Divider />}
                  </Card>
                );
              })}
              {/* 将加载动画放在结果历史内部的最底部 */}
              {loading && (
                <div className="result-loading-bottom">
                  <Spin size="large" />
                  <p style={{ marginTop: 16 }}>正在处理您的请求...</p>
                </div>
              )}
            </div>
          </>
        )}
      </Content>
    </Layout>
  );
};

export default AIDataQuery;