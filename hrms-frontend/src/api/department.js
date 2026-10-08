import request from './request'

// 查询全部部门
export function getDepartments() {
  return request.get('/api/departments')
}

// 新增部门
export function createDepartment(data) {
  return request.post('/api/departments', data)
}

// 更新部门
export function updateDepartment(id, data) {
  return request.put(`/api/departments/${id}`, data)
}

// 删除部门
export function deleteDepartment(id) {
  return request.delete(`/api/departments/${id}`)
}
