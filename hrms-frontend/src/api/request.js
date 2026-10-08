import axios from 'axios'

// 后端地址（跨域访问，后端已配置 CORS）。
// 若改用 vite.config.js 中的代理，请把 baseURL 改为 '/' 或 ''。
const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

// 请求拦截器：自动附加 JWT
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一解包 Result 结构
request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    // 未登录 / 登录过期：清除令牌并回到登录页
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      window.location.reload()
    }
    const msg = error.response?.data?.message || error.message || '请求失败'
    return Promise.reject(new Error(msg))
  }
)

export default request
