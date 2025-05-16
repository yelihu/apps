import { useState } from 'react';
import { Button, DatePicker, version, message, Tabs } from 'antd';
import axios from 'axios';
import './App.css';
import AIDataQuery from './components/AIDataQuery';

function App() {
  const [count, setCount] = useState(0);
  const [helloMessage, setHelloMessage] = useState('');
  const [loading, setLoading] = useState(false);

  // 向 app1 发送请求获取 HelloWorld
  const fetchHelloWorld = async () => {
    setLoading(true);
    try {
      // app1 的端口是 8081
      const response = await axios.get('http://localhost:8081/api/hello');
      setHelloMessage(response.data);
      message.success('成功获取数据！');
    } catch (error) {
      console.error('请求失败:', error);
      message.error('获取数据失败，请检查后端服务是否启动');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <Tabs
        defaultActiveKey="aiDataQuery"
        centered
        items={[
          {
            key: 'aiDataQuery',
            label: 'AI数据查询助手',
            children: <AIDataQuery />,
          },
          {
            key: 'demoPage',
            label: '演示页面',
            children: (
              <header className="App-header">
                <h1>欢迎使用 Apps Frontend</h1>
                <p>这是一个基于 React + TypeScript + Ant Design 的前端应用</p>
                <p>Ant Design 版本: {version}</p>
                
                <div style={{ marginBottom: '20px' }}>
                  <Button type="primary" onClick={() => setCount((count) => count + 1)}>
                    点击计数: {count}
                  </Button>
                </div>

                {/* 新增的请求后端按钮 */}
                <div style={{ marginBottom: '20px' }}>
                  <Button 
                    type="primary" 
                    onClick={fetchHelloWorld} 
                    loading={loading}
                    style={{ marginRight: '10px' }}
                  >
                    获取 HelloWorld
                  </Button>
                  
                  {helloMessage && (
                    <div style={{ marginTop: '10px', padding: '10px', background: '#f0f0f0', borderRadius: '4px' }}>
                      后端返回: <strong>{helloMessage}</strong>
                    </div>
                  )}
                </div>
                
                <DatePicker placeholder="选择日期" />
                <p style={{ fontSize: '16px', marginTop: '20px' }}>
                  编辑 <code>apps-frontend/src/App.tsx</code> 文件并保存，查看热更新效果。
                </p>
                <div style={{ fontSize: '16px' }}>
                <a
                  className="App-link"
                  href="https://reactjs.org"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                    React 文档
                </a>
                {' | '}
                <a
                  className="App-link"
                  href="https://vitejs.dev/guide/features.html"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                    Vite 文档
                </a>
                {' | '}
                <a
                  className="App-link"
                    href="https://ant.design/components/overview-cn/"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                    Ant Design 文档
                </a>
                </div>
              </header>
            ),
          },
        ]}
      />
    </div>
  );
}

export default App; 