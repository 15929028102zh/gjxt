/**
 * 全局 STOMP-over-WebSocket 单例客户端
 *
 * 设计要点:
 *  - 单例,整个 App 共享一条连接,避免 monthly 页面 onHide 断网导致漏推
 *  - 手写 STOMP 帧解析(uni-app 的 uni.connectSocket 是裸 WebSocket,无法用 stompjs)
 *  - 支持 CONNECT / SUBSCRIBE / SEND / MESSAGE / ERROR / 心跳帧
 *  - 内置指数退避自动重连(最多 10 次,3-60s 间隔)
 *  - 通过 on/off/emit 提供事件订阅
 */

import { apiConfig } from '../config/api.uts'

export type WsEvent = 'open' | 'close' | 'error' | 'message' | 'notification' | 'connected' | 'disconnected'
export type WsEventHandler = (data: any | null) => void

class WsClient {
  private isOpen: boolean = false
  private isConnecting: boolean = false
  private socketTask: SocketTask | null = null
  private shouldReconnect: boolean = true

  private reconnectAttempts: number = 0
  private readonly maxReconnectAttempts: number = 10
  private reconnectTimer: number | null = null
  private readonly baseDelay: number = 3000
  private readonly maxDelay: number = 60000

  private subscriptions: Map<string, string> = new Map()
  private listeners: Map<string, WsEventHandler[]> = new Map()

  private lastServerBeatAt: number = 0
  private currentUserId: string = ''

  start(): void {
    this.shouldReconnect = true
    this.tryConnectFromStorage()
  }

  stop(): void {
    this.shouldReconnect = false
    this.clearReconnectTimer()
    this.closeSocket()
    this.subscriptions.clear()
    this.emit('disconnected', { reason: 'manual' })
  }

  notifyLoginChanged(): void {
    this.tryConnectFromStorage()
  }

  on(event: WsEvent, handler: WsEventHandler): () => void {
    const list: WsEventHandler[] = this.listeners.get(event) ?? []
    list.push(handler)
    this.listeners.set(event, list)
    return () => this.off(event, handler)
  }

  off(event: WsEvent, handler: WsEventHandler): void {
    const list: WsEventHandler[] = this.listeners.get(event) ?? []
    const idx = list.indexOf(handler)
    if (idx >= 0) list.splice(idx, 1)
  }

  isConnected(): boolean {
    return this.isOpen
  }

  private tryConnectFromStorage(): void {
    if (this.isOpen || this.isConnecting) return
    const token = uni.getStorageSync('token') as string | null
    const userInfoStr = uni.getStorageSync('userInfo') as string | null
    if (token == null || token == '' || userInfoStr == null || userInfoStr == '') {
      return
    }
    try {
      const parsed = JSON.parse(userInfoStr) as UTSJSONObject
      const userId = (parsed.get('id') as string | null) ?? ''
      if (userId == '') return
      this.currentUserId = userId
      this.connect(token, userId)
    } catch (e) {
      __f__('error','at utils/ws-client.uts:83','[WS] parse userInfo error:', e)
    }
  }

  private connect(token: string, userId: string): void {
    if (this.isOpen || this.isConnecting) return
    this.isConnecting = true

    const wsBase = apiConfig.baseUrl.replace('http://', 'ws://').replace('https://', 'wss://')
    const wsUrl = `${wsBase}/ws/websocket`

    try {
      this.socketTask = uni.connectSocket({
        url: wsUrl,
        protocols: ['v10.stomp', 'v11.stomp', 'v12.stomp'],
        success: () => { /* sent */ },
        fail: () => {
          this.isConnecting = false
          this.scheduleReconnect()
        }
      })
    } catch (e) {
      this.isConnecting = false
      this.scheduleReconnect()
      return
    }

    this.bindSocketListeners(token, userId)
  }

  private bindSocketListeners(token: string, userId: string): void {
    uni.onSocketOpen(() => {
      this.isOpen = true
      this.isConnecting = false
      this.lastServerBeatAt = Date.now()
      this.reconnectAttempts = 0
      this.emit('open', null)

      this.sendConnect(token)
      this.subscribeInternal(`/user/${userId}/queue/notifications`, 'notifications')
    })

    // UTS 不支持内联对象字面量类型声明(UTS110111101),参数类型由 uni.onSocketMessage 签名自动推断
    uni.onSocketMessage((message) => {
      try {
        // STOMP 帧为纯文本,data 正常为 string;非文本帧直接忽略
        const raw = message.data
        if (typeof raw === 'string') {
          this.handleMessage(`${raw}`)
        }
      } catch (e) {
        __f__('error','at utils/ws-client.uts:134','[WS] handle message error:', e)
      }
    })

    uni.onSocketClose(() => {
      this.handleSocketClosed()
    })

    uni.onSocketError(() => {
      this.handleSocketClosed()
    })
  }

  private handleSocketClosed(): void {
    const wasOpen = this.isOpen
    this.isOpen = false
    this.isConnecting = false
    this.socketTask = null
    if (wasOpen) this.emit('close', null)
    if (this.shouldReconnect) {
      this.scheduleReconnect()
    }
  }

  private closeSocket(): void {
    const task = this.socketTask
    if (task != null) {
      try {
        task.close({ code: 1000, reason: 'manual' } as CloseSocketOptions)
      } catch (e) { /* ignore */ }
    }
    this.socketTask = null
    this.isOpen = false
    this.isConnecting = false
  }

  private sendConnect(token: string): void {
    const host = apiConfig.baseUrl.replace(/^https?:\/\//, '')
    const frame = `CONNECT
accept-version:1.0,1.1,1.2
host:${host}
heart-beat:10000,10000
Authorization:Bearer ${token}

` + String.fromCharCode(0)
    this.sendRaw(frame)
  }

  private subscribeInternal(destination: string, channel: string): string {
    const subId = `sub-${this.subscriptions.size}-${Date.now()}`
    this.subscriptions.set(subId, destination)
    const frame = `SUBSCRIBE
id:${subId}
destination:${destination}

` + String.fromCharCode(0)
    this.sendRaw(frame)
    return subId
  }

  private sendRaw(data: string): void {
    const task = this.socketTask
    if (task == null || !this.isOpen) return
    try {
      task.send({
        data: data,
        success: () => { /* ok */ },
        fail: () => __f__('error','at utils/ws-client.uts:201','[WS] send fail')
      } as SendSocketMessageOptions)
    } catch (e) {
      __f__('error','at utils/ws-client.uts:204','[WS] send exception:', e)
    }
  }

  private handleMessage(raw: string): void {
    if (raw === '\n' || raw === '\r\n') {
      this.lastServerBeatAt = Date.now()
      return
    }

    const chunks = raw.split(String.fromCharCode(0))
    for (const chunk of chunks) {
      const trimmed = chunk.trim()
      if (trimmed === '') continue
      this.parseFrame(trimmed)
    }
  }

  private parseFrame(text: string): void {
    const firstNewline = text.indexOf('\n')
    if (firstNewline < 0) return
    const command = text.substring(0, firstNewline).trim()
    const rest = text.substring(firstNewline + 1)

    if (command === 'CONNECTED') {
      this.emit('connected', null)
      return
    }
    if (command === 'ERROR') {
      this.emit('error', { frame: text })
      this.closeSocket()
      this.scheduleReconnect()
      return
    }
    if (command === 'MESSAGE') {
      const lines = rest.split('\n')
      const headers: UTSJSONObject = {}
      let i = 0
      for (; i < lines.length; i++) {
        const line = lines[i]
        if (line === '') break
        const colonIdx = line.indexOf(':')
        if (colonIdx > 0) {
          headers[line.substring(0, colonIdx).trim()] = line.substring(colonIdx + 1).trim()
        }
      }
      const body = lines.slice(i + 1).join('\n')
      const dest = headers.getString('destination') ?? ''
      const payload = { destination: dest, headers: headers, body: body, raw: text }
      this.emit('message', payload)
      if (dest.indexOf('notifications') >= 0) {
        try {
          const obj = JSON.parse(body) as UTSJSONObject
          this.emit('notification', obj)
        } catch (e) {
          __f__('warn','at utils/ws-client.uts:259','[WS] notification json parse fail:', e)
        }
      }
    }
  }

  private clearReconnectTimer(): void {
    const timer: number | null = this.reconnectTimer
    if (timer != null) {
      clearTimeout(timer)
      this.reconnectTimer = null
    }
  }

  private scheduleReconnect(): void {
    if (!this.shouldReconnect) return
    if (this.reconnectAttempts >= this.maxReconnectAttempts) return
    const exp = Math.min(this.reconnectAttempts, 6)
    const base = Math.min(this.baseDelay * Math.pow(2, exp), this.maxDelay)
    const jitter = base * 0.3 * (Math.random() * 2 - 1)
    const delay = Math.max(1000, Math.round(base + jitter))
    this.reconnectAttempts += 1
    this.clearReconnectTimer()
    this.reconnectTimer = setTimeout(() => {
      this.reconnectTimer = null
      this.tryConnectFromStorage()
    }, delay)
  }

  private emit(event: WsEvent, data: any | null): void {
    const list: WsEventHandler[] | null = this.listeners.get(event)
    if (list == null) return
    for (const handler of list) {
      try {
        handler(data)
      } catch (e) {
        __f__('error','at utils/ws-client.uts:295','[WS] event handler error:', e)
      }
    }
  }
}

export const wsClient = new WsClient()
