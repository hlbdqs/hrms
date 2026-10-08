import request from './request'

export function listPermissions() {
  return request.get('/api/permissions')
}
