import request from './request'

// AI 智能问答
export function aiChat(message) {
  return request.post('/api/ai/chat', { message })
}

// AI 文本生成（type: review|weekly|jd）
export function aiGenerate(type, content) {
  return request.post('/api/ai/generate', { type, content })
}
