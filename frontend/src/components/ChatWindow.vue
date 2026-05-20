<template>
  <div class="app-shell">
    <header class="top-bar">
      <div class="top-bar-left">
        <div class="brand-mark">
          <img src="@/assets/logo.png" alt="医伴智答" />
        </div>
        <div class="brand-text">
          <h1>医伴智答</h1>
          <p>仁济综合医院 · 智能伴诊服务</p>
        </div>
      </div>
      <div class="top-bar-actions">
        <span class="status-pill"><span class="dot"></span>服务在线</span>
        <el-button class="new-chat-btn" @click="newChat">
          <i class="fa-solid fa-plus"></i>
          新对话
        </el-button>
      </div>
    </header>

    <main class="workspace">
      <section class="chat-panel">
        <div class="message-scroll" ref="messaggListRef">
          <div v-if="messages.length < 2" class="welcome-block">
            <div class="welcome-banner">
              <div class="banner-icon">
                <i class="fa-solid fa-heart-pulse"></i>
              </div>
              <div>
                <h2>您好，我是医伴智答</h2>
                <p>
                  为仁济综合医院患者提供分诊建议、就医流程与健康管理咨询。
                  请描述症状或选择下方快捷问题开始。
                </p>
              </div>
            </div>

            <div class="feature-grid">
              <article
                v-for="item in featureCards"
                :key="item.title"
                class="feature-card"
                @click="askQuestion(item.prompt)"
              >
                <i :class="item.icon"></i>
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>
              </article>
            </div>
          </div>

          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['msg', message.isUser ? 'msg-user' : 'msg-bot']"
          >
            <div class="msg-avatar">
              <i :class="message.isUser ? 'fa-solid fa-user' : 'fa-solid fa-stethoscope'"></i>
            </div>
            <div class="msg-body">
              <span class="msg-label">{{ message.isUser ? '您' : '医伴智答' }}</span>
              <div class="msg-bubble">
                <div class="msg-content" v-html="message.content"></div>
                <div
                  v-if="message.isThinking || message.isTyping"
                  class="typing-indicator"
                >
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <aside class="info-rail">
        <div class="rail-card highlight">
          <h3><i class="fa-solid fa-hospital"></i> 仁济综合医院</h3>
          <p>门诊时间 8:00–17:30</p>
          <p>急诊 24 小时开放</p>
        </div>
        <div class="rail-card">
          <h3><i class="fa-solid fa-lightbulb"></i> 就诊提示</h3>
          <ul>
            <li>请携带身份证与医保卡</li>
            <li>检验项目请遵医嘱空腹</li>
            <li>线上号源每日 8:00 更新</li>
          </ul>
        </div>
        <div class="rail-card emergency">
          <h3><i class="fa-solid fa-truck-medical"></i> 急救电话</h3>
          <p class="hotline">120</p>
          <p class="sub">危急情况请立即拨打</p>
        </div>
      </aside>
    </main>

    <footer class="composer">
      <div class="quick-row">
        <button
          v-for="q in quickPrompts"
          :key="q"
          type="button"
          class="quick-chip"
          :disabled="isSending"
          @click="askQuestion(q)"
        >
          {{ q }}
        </button>
      </div>
      <div class="input-row">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :autosize="{ minRows: 1, maxRows: 4 }"
          resize="none"
          placeholder="输入症状、科室或就医问题…"
          @keyup.enter="sendMessage"
        />
        <el-button
          type="primary"
          class="send-btn"
          :disabled="isSending"
          @click="sendMessage"
        >
          <i class="fa-solid fa-arrow-up"></i>
        </el-button>
      </div>
      <p class="disclaimer">
        <i class="fa-solid fa-circle-info"></i>
        AI 回复仅供参考，不能替代医生面诊与诊断
      </p>
    </footer>
  </div>
</template>

<script setup>
import { onMounted, ref, watch, nextTick } from 'vue'
import axios from 'axios'

const messaggListRef = ref()
const isSending = ref(false)
const uuid = ref()
const inputMessage = ref('')
const messages = ref([])

const featureCards = [
  {
    title: '智能分诊',
    desc: '根据症状推荐科室',
    icon: 'fa-solid fa-route',
    prompt: '我头痛发热，应该挂哪个科？',
  },
  {
    title: '预约挂号',
    desc: '查询号源与预约流程',
    icon: 'fa-solid fa-calendar-plus',
    prompt: '我想预约明天上午的内科号，怎么操作？',
  },
  {
    title: '检查须知',
    desc: '采血、影像等注意事项',
    icon: 'fa-solid fa-vials',
    prompt: '做血常规需要空腹吗？',
  },
  {
    title: '报告解读',
    desc: '帮助理解常见检验项',
    icon: 'fa-solid fa-file-waveform',
    prompt: '体检报告里白细胞偏高是什么意思？',
  },
]

const quickPrompts = [
  '门诊开放时间',
  '医保怎么用',
  '如何取消预约',
  '儿科在哪个楼',
]

onMounted(() => {
  initUUID()
  watch(messages, () => scrollToBottom(), { deep: true })
  hello()
})

const scrollToBottom = async () => {
  await nextTick()
  if (messaggListRef.value) {
    messaggListRef.value.scrollTo({
      top: messaggListRef.value.scrollHeight,
      behavior: 'smooth',
    })
  }
}

const hello = () => {
  sendRequest('你好')
}

const askQuestion = (text) => {
  if (!isSending.value) {
    inputMessage.value = text
    sendMessage()
  }
}

const sendMessage = () => {
  if (inputMessage.value.trim() && !isSending.value) {
    sendRequest(inputMessage.value.trim())
    inputMessage.value = ''
  }
}

const sendRequest = (message) => {
  isSending.value = true
  const userMsg = { isUser: true, content: message, isTyping: false, isThinking: false }
  if (messages.value.length > 0) messages.value.push(userMsg)

  const botMsg = { isUser: false, content: '', isTyping: true, isThinking: false }
  messages.value.push(botMsg)
  const lastMsg = messages.value[messages.value.length - 1]
  scrollToBottom()

  axios
    .post(
      '/api/xiaozhi/chat',
      { memoryId: uuid.value, message },
      {
        responseType: 'text',
        onDownloadProgress: (e) => {
          const fullText = e.event.target.responseText
          lastMsg.content = convertStreamOutput(fullText)
          scrollToBottom()
        },
      },
    )
    .then(() => {
      lastMsg.isTyping = false
      isSending.value = false
    })
    .catch(() => {
      lastMsg.content = '网络异常，请稍后重试或刷新页面。'
      lastMsg.isTyping = false
      isSending.value = false
    })
}

const initUUID = () => {
  let storedUUID = localStorage.getItem('user_uuid')
  if (!storedUUID) {
    storedUUID = Math.floor(Math.random() * 1000000)
    localStorage.setItem('user_uuid', storedUUID)
  }
  uuid.value = storedUUID
}

const convertStreamOutput = (output) => {
  if (!output) return ''
  return output
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
}

const newChat = () => {
  localStorage.removeItem('user_uuid')
  window.location.reload()
}
</script>

<style scoped>
.app-shell {
  --primary: #0d9488;
  --primary-dark: #0f766e;
  --primary-soft: #ccfbf1;
  --surface: #ffffff;
  --bg: #ecfdf5;
  --text: #134e4a;
  --text-muted: #5f7a76;
  --border: #d1e7e3;
  --user-bg: #0d9488;
  --bot-bg: #f8fffe;

  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  background: linear-gradient(160deg, #ecfdf5 0%, #f8fafc 45%, #ffffff 100%);
  font-family: 'Segoe UI', system-ui, -apple-system, sans-serif;
  color: var(--text);
  overflow: hidden;
}

.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 28px;
  background: var(--surface);
  border-bottom: 1px solid var(--border);
  box-shadow: 0 4px 20px rgba(13, 148, 136, 0.06);
  flex-shrink: 0;
  z-index: 10;
}

.top-bar-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.brand-mark {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  overflow: hidden;
  border: 2px solid var(--primary-soft);
  background: #fff;
}

.brand-mark img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.brand-text h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.brand-text p {
  margin: 2px 0 0;
  font-size: 13px;
  color: var(--text-muted);
}

.top-bar-actions {
  display: flex;
  align-items: center;
  gap: 14px;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--primary-dark);
  background: var(--primary-soft);
  padding: 6px 12px;
  border-radius: 999px;
}

.status-pill .dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #22c55e;
  animation: pulse 2s infinite;
}

.new-chat-btn {
  background: var(--primary) !important;
  border: none !important;
  border-radius: 10px !important;
  font-weight: 600 !important;
}

.workspace {
  flex: 1;
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 0;
  min-height: 0;
  overflow: hidden;
}

.chat-panel {
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 0;
}

.message-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 28px 32px 16px;
  max-width: 900px;
  margin: 0 auto;
  width: 100%;
}

.welcome-block {
  margin-bottom: 24px;
}

.welcome-banner {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 20px;
  padding: 24px 28px;
  box-shadow: 0 8px 30px rgba(13, 148, 136, 0.08);
}

.banner-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--primary), #14b8a6);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  flex-shrink: 0;
}

.welcome-banner h2 {
  margin: 0 0 8px;
  font-size: 22px;
}

.welcome-banner p {
  margin: 0;
  color: var(--text-muted);
  line-height: 1.65;
  font-size: 15px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px;
  margin-top: 20px;
}

.feature-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 18px 20px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s, border-color 0.2s;
}

.feature-card:hover {
  transform: translateY(-2px);
  border-color: var(--primary);
  box-shadow: 0 10px 24px rgba(13, 148, 136, 0.12);
}

.feature-card i {
  font-size: 22px;
  color: var(--primary);
  margin-bottom: 10px;
}

.feature-card h3 {
  margin: 0 0 6px;
  font-size: 16px;
}

.feature-card p {
  margin: 0;
  font-size: 13px;
  color: var(--text-muted);
}

.msg {
  display: flex;
  gap: 12px;
  margin-bottom: 22px;
}

.msg-user {
  flex-direction: row-reverse;
}

.msg-avatar {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 16px;
}

.msg-bot .msg-avatar {
  background: var(--primary-soft);
  color: var(--primary-dark);
}

.msg-user .msg-avatar {
  background: #e0f2fe;
  color: #0369a1;
}

.msg-body {
  max-width: min(78%, 640px);
}

.msg-label {
  display: block;
  font-size: 12px;
  color: var(--text-muted);
  margin-bottom: 6px;
}

.msg-user .msg-label {
  text-align: right;
}

.msg-bubble {
  padding: 14px 18px;
  border-radius: 16px;
  font-size: 15px;
  line-height: 1.65;
  word-break: break-word;
}

.msg-bot .msg-bubble {
  background: var(--bot-bg);
  border: 1px solid var(--border);
  border-top-left-radius: 4px;
}

.msg-user .msg-bubble {
  background: var(--user-bg);
  color: #fff;
  border-top-right-radius: 4px;
}

.msg-user .msg-content {
  color: #fff;
}

.info-rail {
  background: var(--surface);
  border-left: 1px solid var(--border);
  padding: 24px 20px;
  overflow-y: auto;
}

.rail-card {
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 16px 18px;
  margin-bottom: 14px;
}

.rail-card h3 {
  margin: 0 0 10px;
  font-size: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.rail-card h3 i {
  color: var(--primary);
}

.rail-card p,
.rail-card li {
  margin: 0 0 6px;
  font-size: 13px;
  color: var(--text-muted);
  line-height: 1.5;
}

.rail-card ul {
  margin: 0;
  padding-left: 18px;
}

.rail-card.highlight {
  background: linear-gradient(135deg, #0d9488, #14b8a6);
  border: none;
  color: #fff;
}

.rail-card.highlight h3,
.rail-card.highlight p {
  color: #fff;
}

.rail-card.highlight h3 i {
  color: #fff;
}

.rail-card.emergency {
  background: #fef2f2;
  border-color: #fecaca;
}

.rail-card.emergency h3 {
  color: #b91c1c;
}

.rail-card.emergency h3 i {
  color: #dc2626;
}

.hotline {
  font-size: 28px !important;
  font-weight: 800;
  color: #b91c1c !important;
  margin: 4px 0 !important;
}

.rail-card .sub {
  font-size: 12px !important;
}

.composer {
  flex-shrink: 0;
  padding: 12px 28px 18px;
  background: var(--surface);
  border-top: 1px solid var(--border);
}

.quick-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  max-width: 900px;
  margin: 0 auto 10px;
}

.quick-chip {
  border: 1px solid var(--border);
  background: var(--bg);
  color: var(--primary-dark);
  border-radius: 999px;
  padding: 6px 14px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.quick-chip:hover:not(:disabled) {
  background: var(--primary-soft);
  border-color: var(--primary);
}

.quick-chip:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.input-row {
  display: flex;
  align-items: flex-end;
  gap: 10px;
  max-width: 900px;
  margin: 0 auto;
  background: #f8fafc;
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 8px 10px 8px 16px;
}

.input-row:focus-within {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(13, 148, 136, 0.12);
}

.input-row :deep(.el-textarea__inner) {
  border: none;
  background: transparent;
  box-shadow: none;
  font-size: 15px;
  padding: 6px 0;
}

.send-btn {
  width: 44px;
  height: 44px;
  min-width: 44px;
  border-radius: 12px !important;
  padding: 0 !important;
  background: var(--primary) !important;
  border: none !important;
}

.disclaimer {
  text-align: center;
  font-size: 12px;
  color: var(--text-muted);
  margin: 10px 0 0;
}

.typing-indicator .dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  background: var(--text-muted);
  border-radius: 50%;
  margin-right: 3px;
  animation: typing 1s infinite;
}

.typing-indicator .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator .dot:nth-child(3) {
  animation-delay: 0.4s;
}

.message-scroll::-webkit-scrollbar {
  width: 6px;
}

.message-scroll::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 8px;
}

@media (max-width: 960px) {
  .info-rail {
    display: none;
  }

  .workspace {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .top-bar {
    padding: 12px 16px;
  }

  .status-pill {
    display: none;
  }

  .brand-text h1 {
    font-size: 17px;
  }

  .message-scroll {
    padding: 16px;
  }

  .welcome-banner {
    flex-direction: column;
    padding: 18px;
  }

  .feature-grid {
    grid-template-columns: 1fr;
  }

  .composer {
    padding: 10px 14px 14px;
  }

  .msg-body {
    max-width: 88%;
  }
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

@keyframes typing {
  0%,
  100% {
    opacity: 0.3;
    transform: translateY(0);
  }
  50% {
    opacity: 1;
    transform: translateY(-4px);
  }
}
</style>
