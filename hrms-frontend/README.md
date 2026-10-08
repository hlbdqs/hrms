# 智能人事管理系统 - 前端（hrms-frontend）

> 实验二：前后端分离与 RESTful API（Vue 3 + Vite + Element Plus + Axios）。

## 技术栈

- Vue 3（`<script setup>` 组合式 API）
- Vite
- Element Plus（UI 组件库）
- Axios（HTTP 请求）

## 目录结构

```
hrms-frontend
├── index.html
├── package.json
├── vite.config.js
└── src
    ├── main.js                 # 入口，注册 Element Plus
    ├── App.vue                 # 布局 + 页面挂载
    ├── api/
    │   ├── request.js          # axios 实例（baseURL + 响应拦截）
    │   ├── employee.js         # 员工接口
    │   └── department.js       # 部门接口
    └── views/
        └── EmployeeList.vue    # 员工列表页（查询/分页/新增/编辑/删除）
```

## 运行前准备

1. 确保后端已启动（`hrms-backend`，端口 8080）。
2. 安装依赖：

```bash
cd hrms-frontend
npm install
```

## 运行

```bash
npm run dev
```

浏览器访问 <http://localhost:5173>。

## 跨域说明

- 后端已配置 CORS（`hrms-backend/src/main/java/com/hrms/config/CorsConfig.java`），允许前端跨域访问 `/api/**`。
- 前端 axios `baseURL` 指向 `http://localhost:8080`，属真实跨域，用于验证 CORS。
- 备选方案：启用 `vite.config.js` 中的 `proxy` 代理，并把 `request.js` 的 `baseURL` 改为 `/`。

## 构建部署

```bash
npm run build   # 产物在 dist/
npm run preview # 本地预览构建产物
```
