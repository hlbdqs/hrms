<script setup>
import EmployeeList from './views/EmployeeList.vue'
</script>

<template>
  <div class="app">
    <header class="app-header">
      <h1>智能人事管理系统</h1>
      <span class="sub">用户中心 · 实验二：前后端分离联调</span>
    </header>
    <main class="app-main">
      <EmployeeList />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  background: #f5f7fa;
  font-family: -apple-system, "Segoe UI", "PingFang SC", "Microsoft YaHei", sans-serif;
}
.app-header {
  height: 60px;
  background: #409eff;
  color: #fff;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 0 24px;
}
.app-header h1 {
  font-size: 20px;
  font-weight: 600;
}
.app-header .sub {
  font-size: 13px;
  opacity: 0.85;
}
.app-main {
  padding: 24px;
}
</style>
