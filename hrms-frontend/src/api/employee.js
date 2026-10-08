import axios from 'axios'
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

// 查询员工角色
export function getEmployeeRoles(id) {
  return request.get(`/api/employees/${id}/roles`)
}

// 分配员工角色
export function assignEmployeeRoles(id, roleIds) {
  return request.put(`/api/employees/${id}/roles`, roleIds)
}

// 管理员重置员工密码
export function resetEmployeePassword(id, data) {
  return request.put(`/api/employees/${id}/password`, data)
}

// 导出员工（返回文件流，需原始 blob，单独用 axios 处理）
export async function exportEmployees() {
  const token = localStorage.getItem('token')
  const res = await axios.get('http://localhost:8080/api/employees/export', {
    responseType: 'blob',
    headers: { Authorization: `Bearer ${token}` }
  })
  return res.data
}

// 导入员工（FormData 上传）
export function importEmployees(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/employees/import', formData)
}
