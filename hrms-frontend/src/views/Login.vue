<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'

const emit = defineEmits(['success'])

const username = ref('')
const password = ref('')
const loading = ref(false)

async function handleLogin() {
  if (!username.value || !password.value) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login({ username: username.value, password: password.value })
    if (res.code === 200) {
      // 注意：此处为演示将 token 存 localStorage；生产建议 httpOnly Cookie 或内存短期存储
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('authorities', JSON.stringify(res.data.authorities || []))
      ElMessage.success('登录成功')
      emit('success', res.data.username, res.data.authorities || [])
    } else {
      ElMessage.error(res.message || '登录失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2 class="title">智能人事管理系统</h2>
      <p class="subtitle">请登录</p>
      <el-form @submit.prevent>
        <el-form-item>
          <el-input v-model="username" placeholder="用户名" clearable @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="password" type="password" placeholder="密码" show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-button type="primary" class="submit" :loading="loading" @click="handleLogin">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #409eff 0%, #2c6fd1 100%);
}
.login-card {
  width: 380px;
  padding: 8px 12px;
}
.title {
  text-align: center;
  margin-bottom: 4px;
}
.subtitle {
  text-align: center;
  color: #909399;
  margin-bottom: 20px;
}
.submit {
  width: 100%;
}
</style>
