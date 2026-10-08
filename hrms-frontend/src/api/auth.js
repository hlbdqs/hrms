import request from './request'

// 登录
export function login(data) {
  return request.post('/api/auth/login', data)
}

// 修改当前用户密码
export function changePassword(data) {
  return request.put('/api/auth/password', data)
}
