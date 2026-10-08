// 权限工具：从 localStorage 读取当前登录用户的权限（authorities）并做判断。
// 注意：前端隐藏按钮仅为改善体验，真正的安全边界在后端 @PreAuthorize。

export function getAuthorities() {
  try {
    return JSON.parse(localStorage.getItem('authorities') || '[]')
  } catch {
    return []
  }
}

export function hasAuthority(code) {
  return getAuthorities().includes(code)
}

export function isAdmin() {
  return hasAuthority('ROLE_ADMIN')
}
