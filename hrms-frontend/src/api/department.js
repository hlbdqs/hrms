import request from './request'

// 查询全部部门
export function getDepartments() {
  return request.get('/api/departments')
}
