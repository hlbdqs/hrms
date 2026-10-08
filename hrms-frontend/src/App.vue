<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import EmployeeList from './views/EmployeeList.vue'
import DepartmentManage from './views/DepartmentManage.vue'
import RoleManage from './views/RoleManage.vue'
import LogManage from './views/LogManage.vue'
import { changePassword } from './api/auth'

const username = ref(localStorage.getItem('username') || '')
const authorities = ref(JSON.parse(localStorage.getItem('authorities') || '[]'))
const isAdmin = computed(() => authorities.value.includes('ROLE_ADMIN'))
const activeTab = ref('dashboard')

const pwdDialogVisible = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '' })

function handleLogin(u, auths) {
  username.value = u
  authorities.value = auths
}

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  localStorage.removeItem('authorities')
  username.value = ''
  authorities.value = []
  activeTab.value = 'dashboard'
}

async function handleChangePassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning('请输入原密码和新密码')
    return
  }
  try {
    const res = await changePassword(pwdForm)
    if (res.code === 200) {
      ElMessage.success('密码修改成功，请重新登录')
      pwdDialogVisible.value = false
      pwdForm.oldPassword = ''
      pwdForm.newPassword = ''
      handleLogout()
    } else {
      ElMessage.error(res.message || '修改失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '修改失败')
  }
}
</script>

<template>
  <div v-if="!username" class="login-wrap">
    <Login @success="handleLogin" />
  </div>

  <div v-else class="app">
    <header class="app-header">
      <h1>智能人事管理系统</h1>
      <nav class="nav">
        <button :class="{ active: activeTab === 'dashboard' }" @click="activeTab = 'dashboard'">仪表盘</button>
        <button :class="{ active: activeTab === 'employees' }" @click="activeTab = 'employees'">员工管理</button>
        <button :class="{ active: activeTab === 'departments' }" @click="activeTab = 'departments'">部门管理</button>
        <button v-if="isAdmin" :class="{ active: activeTab === 'roles' }" @click="activeTab = 'roles'">角色权限</button>
        <button v-if="isAdmin" :class="{ active: activeTab === 'logs' }" @click="activeTab = 'logs'">审计日志</button>
      </nav>
      <div class="spacer"></div>
      <span class="user">{{ username }}</span>
      <button class="btn" @click="pwdDialogVisible = true">修改密码</button>
      <button class="btn" @click="handleLogout">退出登录</button>
    </header>

    <main class="app-main">
      <Dashboard v-if="activeTab === 'dashboard'" />
      <EmployeeList v-else-if="activeTab === 'employees'" />
      <DepartmentManage v-else-if="activeTab === 'departments'" />
      <RoleManage v-else-if="activeTab === 'roles'" />
      <LogManage v-else-if="activeTab === 'logs'" />
    </main>

    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="400px">
      <el-form label-width="80px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="至少 6 位" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword">确定</el-button>
      </template>
    </el-dialog>
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
  gap: 20px;
  padding: 0 24px;
}
.app-header h1 {
  font-size: 20px;
  font-weight: 600;
  white-space: nowrap;
}
.nav {
  display: flex;
  gap: 8px;
}
.nav button {
  border: none;
  background: transparent;
  color: #fff;
  padding: 6px 14px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}
.nav button:hover {
  background: rgba(255, 255, 255, 0.15);
}
.nav button.active {
  background: rgba(255, 255, 255, 0.28);
  font-weight: 600;
}
.spacer {
  flex: 1;
}
.app-header .user {
  font-size: 14px;
}
.app-header .btn {
  border: 1px solid rgba(255, 255, 255, 0.6);
  background: transparent;
  color: #fff;
  padding: 5px 14px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}
.app-header .btn:hover {
  background: rgba(255, 255, 255, 0.15);
}
.app-main {
  padding: 24px;
}
</style>
