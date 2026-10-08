import request from './request'

export function listRoles() {
  return request.get('/api/roles')
}

export function getRolePermissions(id) {
  return request.get(`/api/roles/${id}/permissions`)
}

export function createRole(data) {
  return request.post('/api/roles', data)
}

export function updateRole(id, data) {
  return request.put(`/api/roles/${id}`, data)
}

export function deleteRole(id) {
  return request.delete(`/api/roles/${id}`)
}

export function assignRolePermissions(id, permissionIds) {
  return request.put(`/api/roles/${id}/permissions`, permissionIds)
}
