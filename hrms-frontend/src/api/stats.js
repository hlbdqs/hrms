import request from './request'

export function getOverview() {
  return request.get('/api/stats/overview')
}

export function getByDepartment() {
  return request.get('/api/stats/by-department')
}

export function getByGender() {
  return request.get('/api/stats/by-gender')
}

export function getByStatus() {
  return request.get('/api/stats/by-status')
}
