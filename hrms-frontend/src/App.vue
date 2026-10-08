<script setup>
import { ref } from 'vue'
import Login from './views/Login.vue'
import EmployeeList from './views/EmployeeList.vue'

const username = ref(localStorage.getItem('username') || '')

function handleLogin(u) {
  username.value = u
}

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  username.value = ''
}
</script>

<template>
  <div v-if="!username" class="login-wrap">
    <Login @success="handleLogin" />
  </div>

  <div v-else class="app">
    <header class="app-header">
      <h1>智能人事管理系统</h1>
      <span class="sub">实验二/四：前后端分离 + 登录权限</span>
      <div class="spacer"></div>
      <span class="user">当前用户：{{ username }}</span>
      <button class="logout" @click="handleLogout">退出登录</button>
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
.spacer {
  flex: 1;
}
.app-header .user {
  font-size: 14px;
}
.app-header .logout {
  border: 1px solid rgba(255, 255, 255, 0.6);
  background: transparent;
  color: #fff;
  padding: 5px 14px;
  border-radius: 4px;
  cursor: pointer;
}
.app-header .logout:hover {
  background: rgba(255, 255, 255, 0.15);
}
.app-main {
  padding: 24px;
}
</style>
