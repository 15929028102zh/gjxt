/**
 * 全局通知 store(响应式)
 *
 * 设计要点:
 *  - 整个 App 共享一份通知数据,各页面(月检 / 首页 / 周检 / 我的)都从这里读取
 *  - 由 ws-client 推送触发增量更新,由 fetchFromServer 触发全量同步
 *  - WebSocket 离线时启动 60s 兜底轮询
 *  - 限制通知列表最多保留 100 条
 */

import { reactive } from 'vue'
import { wsClient } from '../utils/ws-client.uts'
import { getApiUrl } from '../config/api.uts'

export type NotificationItem = {
  id: string
  title: string
  content: string
  isRead: boolean
  createTime: string
  taskId: string | null
  taskNo: string | null
  type: string | null
  priority: string | null
  actionUrl: string | null
}

// reactive 入参先断言为具名类型(与 config/api.uts 一致),避免属性退化为 Any? 导致方法调用无法编译
type NotificationState = {
  list: NotificationItem[]
  isLoaded: boolean
  lastSyncAt: number
  initialized: boolean
}

const state = reactive({
  list: [] as NotificationItem[],
  isLoaded: false,
  lastSyncAt: 0,
  initialized: false
} as NotificationState)

// 退订函数列表与轮询定时器仅内部记账、不参与渲染,故单独声明为模块级强类型变量
const unsubscribers: Array<() => void> = []
let pollingTimer: number | null = null

export const notificationStore = {
  get list(): NotificationItem[] {
    return state.list
  },
  get unreadCount(): number {
    let n = 0
    for (const item of state.list) {
      if (!item.isRead) n++
    }
    return n
  },
  get isLoaded(): boolean {
    return state.isLoaded
  }
}

export function initNotificationStore(): void {
  if (state.initialized) return
  state.initialized = true

  // WsEventHandler 签名为 (data: any | null) => void,回调参数类型必须与之一致
  const offNotification = wsClient.on('notification', (raw: any | null) => {
    if (raw == null) return
    addOrUpdateNotification(parseNotification(raw as UTSJSONObject))
  })
  const offConnected = wsClient.on('connected', () => {
    fetchFromServer().catch(() => { /* ignore */ })
  })
  const offDisconnected = wsClient.on('disconnected', () => {
    startPollingFallback()
  })
  unsubscribers.push(offNotification, offConnected, offDisconnected)

  fetchFromServer().catch(() => { /* ignore */ })
}

export function destroyNotificationStore(): void {
  for (const off of unsubscribers) {
    try { off() } catch (e) { /* ignore */ }
  }
  unsubscribers.splice(0, unsubscribers.length)
  stopPollingFallback()
  state.initialized = false
}

export function fetchFromServer(): Promise<void> {
  const token = uni.getStorageSync('token') as string | null
  if (token == null || token == '') return Promise.resolve()

  return new Promise((resolve) => {
    uni.request({
      url: getApiUrl('/notifications/user/notifications'),
      method: 'GET',
      header: { 'Authorization': 'Bearer ' + token },
      success: (res) => {
        try {
          // 后端直接返回通知数组(ResponseEntity<List<Notification>>),res.data 即数组
          const body = res.data as Array<UTSJSONObject>
          const items: NotificationItem[] = []
          for (const n of body) {
            const item: NotificationItem = {
              id: (n.get('id') as string | null) ?? '',
              title: (n.get('title') as string | null) ?? '',
              content: (n.get('content') as string | null) ?? '',
              isRead: (n.get('isRead') as boolean | null) ?? false,
              createTime: (n.get('createTime') as string | null) ?? '',
              taskId: n.get('taskId') as string | null,
              taskNo: n.get('taskNo') as string | null,
              type: n.get('type') as string | null,
              priority: n.get('priority') as string | null,
              actionUrl: n.get('actionUrl') as string | null
            }
            items.push(item)
          }
          state.list = items
          trimList()
          state.isLoaded = true
          state.lastSyncAt = Date.now()
        } catch (e) {
          console.warn('[NotificationStore] parse fail:', e)
        }
        resolve()
      },
      fail: () => {
        resolve()
      }
    })
  })
}

export function addOrUpdateNotification(n: NotificationItem): void {
  if (n.id == null || n.id == '') return
  const idx = state.list.findIndex(x => x.id === n.id)
  if (idx >= 0) {
    state.list[idx] = n
  } else {
    state.list.unshift(n)
    trimList()
    try {
      uni.showToast({
        title: '新通知: ' + n.title,
        icon: 'none',
        duration: 2500
      })
    } catch (e) { /* ignore */ }
  }
}

export function markRead(id: string): Promise<void> {
  const item = state.list.find(x => x.id === id)
  if (item == null) return Promise.resolve()
  if (item.isRead) return Promise.resolve()
  item.isRead = true
  const token = uni.getStorageSync('token') as string | null
  if (token == null || token == '') return Promise.resolve()
  return new Promise((resolve) => {
    uni.request({
      url: getApiUrl('/notifications/read/' + id),
      method: 'PUT',
      header: { 'Authorization': 'Bearer ' + token },
      success: () => resolve(),
      fail: () => resolve()
    })
  })
}

export function markAllRead(): Promise<void> {
  for (const item of state.list) {
    item.isRead = true
  }
  const token = uni.getStorageSync('token') as string | null
  if (token == null || token == '') return Promise.resolve()
  return new Promise((resolve) => {
    uni.request({
      url: getApiUrl('/notifications/read-all'),
      method: 'PUT',
      header: { 'Authorization': 'Bearer ' + token },
      success: () => resolve(),
      fail: () => resolve()
    })
  })
}

export function removeNotification(id: string): Promise<void> {
  const idx = state.list.findIndex(x => x.id === id)
  if (idx >= 0) state.list.splice(idx, 1)
  const token = uni.getStorageSync('token') as string | null
  if (token == null || token == '') return Promise.resolve()
  return new Promise((resolve) => {
    uni.request({
      url: getApiUrl('/notifications/' + id),
      method: 'DELETE',
      header: { 'Authorization': 'Bearer ' + token },
      success: () => resolve(),
      fail: () => resolve()
    })
  })
}

function trimList(): void {
  if (state.list.length > 100) {
    state.list = state.list.slice(0, 100)
  }
}

function parseNotification(raw: UTSJSONObject): NotificationItem {
  return {
    id: (raw.get('id') as string | null) ?? '',
    title: (raw.get('title') as string | null) ?? '',
    content: (raw.get('content') as string | null) ?? '',
    isRead: false,
    createTime: (raw.get('createTime') as string | null) ?? '',
    taskId: raw.get('taskId') as string | null,
    taskNo: raw.get('taskNo') as string | null,
    type: raw.get('type') as string | null,
    priority: raw.get('priority') as string | null,
    actionUrl: raw.get('actionUrl') as string | null
  }
}

function startPollingFallback(): void {
  stopPollingFallback()
  pollingTimer = setInterval(() => {
    if (!wsClient.isConnected()) {
      fetchFromServer().catch(() => { /* ignore */ })
    }
  }, 60000)
}

function stopPollingFallback(): void {
  if (pollingTimer != null) {
    clearInterval(pollingTimer!)
    pollingTimer = null
  }
}
