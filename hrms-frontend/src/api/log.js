import request from './request'

// 分页查询审计日志
export function getLogPage(params) {
  return request.get('/api/logs', { params })
}
