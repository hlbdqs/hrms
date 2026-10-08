<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { aiChat, aiGenerate } from '../api/ai'

// ---- 问答区 ----
const messages = ref([])
const question = ref('')
const chatting = ref(false)

async function handleChat() {
  const q = question.value.trim()
  if (!q) {
    ElMessage.warning('请输入问题')
    return
  }
  messages.value.push({ role: 'user', content: q })
  question.value = ''
  chatting.value = true
  try {
    const res = await aiChat(q)
    if (res.code === 200) {
      messages.value.push({ role: 'assistant', content: res.data.reply })
    } else {
      messages.value.push({ role: 'assistant', content: res.message || '调用失败' })
    }
  } catch (e) {
    messages.value.push({ role: 'assistant', content: '调用失败：' + (e.message || '') })
  } finally {
    chatting.value = false
    scrollToBottom()
  }
}

function scrollToBottom() {
  nextTick(() => {
    const el = document.getElementById('chat-box')
    if (el) {
      el.scrollTop = el.scrollHeight
    }
  })
}

// ---- 生成区 ----
const genType = ref('review')
const genContent = ref('')
const genResult = ref('')
const generating = ref(false)

async function handleGenerate() {
  const c = genContent.value.trim()
  if (!c) {
    ElMessage.warning('请输入内容')
    return
  }
  generating.value = true
  genResult.value = ''
  try {
    const res = await aiGenerate(genType.value, c)
    if (res.code === 200) {
      genResult.value = res.data.reply
    } else {
      ElMessage.error(res.message || '生成失败')
    }
  } catch (e) {
    ElMessage.error(e.message || '生成失败')
  } finally {
    generating.value = false
  }
}
</script>

<template>
  <div class="ai-assistant">
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card>
          <div class="card-title">AI 智能问答</div>
          <div id="chat-box" class="chat-box">
            <div v-if="messages.length === 0" class="empty">
              问我人事数据，例如「研发部有多少人？」「张三的邮箱是什么？」
            </div>
            <div v-for="(m, i) in messages" :key="i" :class="['msg', m.role]">
              <div class="bubble">{{ m.content }}</div>
            </div>
            <div v-if="chatting" class="msg assistant">
              <div class="bubble">思考中…</div>
            </div>
          </div>
          <div class="chat-input">
            <el-input v-model="question" placeholder="输入问题，回车发送" @keyup.enter="handleChat" />
            <el-button type="primary" :loading="chatting" @click="handleChat">发送</el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="10">
        <el-card>
          <div class="card-title">AI 文本生成</div>
          <el-form label-width="60px">
            <el-form-item label="类型">
              <el-select v-model="genType" style="width: 100%">
                <el-option label="绩效评语" value="review" />
                <el-option label="周报" value="weekly" />
                <el-option label="岗位 JD" value="jd" />
              </el-select>
            </el-form-item>
            <el-form-item label="内容">
              <el-input
                v-model="genContent"
                type="textarea"
                :rows="6"
                placeholder="输入员工信息 / 工作内容 / 岗位信息…"
              />
            </el-form-item>
          </el-form>
          <el-button type="primary" :loading="generating" style="width: 100%" @click="handleGenerate">
            生成
          </el-button>
          <div v-if="genResult" class="gen-result">{{ genResult }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}
.chat-box {
  height: 380px;
  overflow-y: auto;
  background: #f5f7fa;
  border-radius: 6px;
  padding: 16px;
  margin-bottom: 12px;
}
.empty {
  color: #c0c4cc;
  text-align: center;
  margin-top: 140px;
}
.msg {
  display: flex;
  margin-bottom: 12px;
}
.msg.user {
  justify-content: flex-end;
}
.msg.assistant {
  justify-content: flex-start;
}
.bubble {
  max-width: 75%;
  padding: 10px 14px;
  border-radius: 10px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}
.msg.user .bubble {
  background: #409eff;
  color: #fff;
}
.msg.assistant .bubble {
  background: #fff;
  color: #303133;
  border: 1px solid #e4e7ed;
}
.chat-input {
  display: flex;
  gap: 8px;
}
.gen-result {
  margin-top: 16px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  line-height: 1.8;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
