import request from './request'

// 分页 + 关键字查询
export function getEmployeePage(params) {
  return request.get('/api/employees', { params })
}

// 按 ID 查询
export function getEmployee(id) {
  return request.get(`/api/employees/${id}`)
}

// 新增
export function createEmployee(data) {
  return request.post('/api/employees', data)
}

// 更新
export function updateEmployee(id, data) {
  return request.put(`/api/employees/${id}`, data)
}

// 删除
export function deleteEmployee(id) {
  return request.delete(`/api/employees/${id}`)
}
