import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    // 方案二（可选）：用开发代理转发 /api 到后端，替代后端 CORS。
    // 若启用代理，请把 src/api/request.js 中的 baseURL 改为 '/' 或空字符串。
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8080',
    //     changeOrigin: true
    //   }
    // }
  }
})
