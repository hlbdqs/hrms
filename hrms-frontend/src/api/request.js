import axios from 'axios'

// 后端地址（跨域访问，后端已配置 CORS）。
// 若改用 vite.config.js 中的代理，请把 baseURL 改为 '/' 或 ''。
const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

// 响应拦截器：统一解包 Result 结构
request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const msg = error.response?.data?.message || error.message || '请求失败'
    return Promise.reject(new Error(msg))
  }
)

export default request
