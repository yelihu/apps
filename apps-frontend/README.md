# Apps Frontend

这是一个基于 React、TypeScript 和 Ant Design 构建的前端应用项目。

## 技术栈

- React 18
- TypeScript
- Ant Design 5
- Vite

## 开发指南

### 安装依赖

```bash
cd apps-frontend
npm install
# 或者使用 yarn
yarn
# 或者使用 pnpm
pnpm install
```

### 启动开发服务器

```bash
npm run dev
# 或者使用 yarn
yarn dev
# 或者使用 pnpm
pnpm dev
```

启动成功后，通常可以通过访问 [http://localhost:5173](http://localhost:5173) 来查看应用。

### 构建生产版本

```bash
npm run build
# 或者使用 yarn
yarn build
# 或者使用 pnpm
pnpm build
```

构建完成后，生成的文件会保存在 `dist` 目录中。

## 项目结构

```
apps-frontend/
├── public/               # 静态资源目录
├── src/                  # 源代码目录
│   ├── App.tsx           # 主应用组件
│   ├── App.css           # 主应用样式
│   ├── main.tsx          # 入口文件
│   └── index.css         # 全局样式
├── index.html            # HTML 模板
├── package.json          # 项目依赖配置
├── tsconfig.json         # TypeScript 配置
├── vite.config.ts        # Vite 配置
└── README.md             # 项目说明文档
```

## 渐进式开发路线

本项目遵循渐进式开发方法，详细的发展规划请参阅 `doc/frontend_development_plan.md`。 