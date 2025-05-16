import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import App from './App.tsx'
import DemoPage from './pages/DemoPage.tsx'
import AIDataQuery from './components/AIDataQuery/AIDataQuery.tsx'
// import 'antd/dist/reset.css'; // Ant Design 5.x 推荐引入的重置样式
import './index.css' // 我们可以创建一个空的 index.css 或者包含一些基本样式

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/demo" element={<DemoPage />} />
        <Route path="/ai-data-query" element={<AIDataQuery />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
) 