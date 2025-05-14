import React, { useState } from 'react';
import { Button, DatePicker, version } from 'antd';
import './App.css'; // 可选的组件特定样式

function App() {
  const [count, setCount] = useState(0);

  return (
    <div className="App">
      <header className="App-header">
        <h1>欢迎使用 Apps Frontend</h1>
        <p>这是一个基于 React + TypeScript + Ant Design 的前端应用</p>
        <p>Ant Design 版本: {version}</p>
        <div style={{ marginBottom: '20px' }}>
          <Button type="primary" onClick={() => setCount((count) => count + 1)}>
            点击计数: {count}
          </Button>
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
    </div>
  );
}

export default App; 