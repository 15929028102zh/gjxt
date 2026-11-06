/***
 * WebSocket 管理器
 * @author ML
 * @date 2024/12/19
 * @example const ws = WebSocketManager.getInstance('ws://localhost:3000',{method:'POST'});
 * ws.onOpen(() => { console.log('连接已建立'); });
 * ws.onClose(() => { console.log('连接已关闭'); });
 * ws.onError((error) => { console.error('连接错误', error); });
 * ws.onMessage((message) => { console.log('收到消息', message); });
 * ws.connect(); // 连接
 * ws.send('Hello, WebSocket!'); //发送消息
 * ws.close() //关闭连接
 **/
import timer from './inteval';

// 定义定时器名称
const timerName = {
	ws: Symbol('websocket'),
}

interface UniAppWebSocket {
	send(options : UniApp.SendSocketMessageOptions) : void;
	close(options : UniApp.CloseSocketOptions) : void;
	onOpen(callback : (result : UniApp.OnSocketOpenCallbackResult) => void) : void;
	onClose(callback : (result : any) => void) : void;
	onError(callback : (result : UniApp.GeneralCallbackResult) => void) : void;
	onMessage(callback : (result : UniApp.OnSocketMessageCallbackResult) => void) : void;
}

class WebSocketManager {
	private url : string;
	private options : UniApp.ConnectSocketOption;
	private socketTask : UniAppWebSocket | null = null;
	private isConnected : boolean = false;
	private reconnectInterval : number = 3000; // 重连间隔时间（毫秒）
	private heartbeatInterval : number = 5000; // 心跳间隔时间（毫秒）
	private reconnectAttempts : number = 0; // 当前重连次数
	private maxReconnectAttempts : number = 10; // 最大重连次数,超过后不再重连
	private reconnectTimer : any = null;
	private onOpenCallbacks : Array<() => void> = [];
	private onCloseCallbacks : Array<() => void> = [];
	private onErrorCallbacks : Array<(error : any) => void> = [];
	private onMessageCallbacks : Array<(message : any) => void> = [];
	private static connections : Map<string, WebSocketManager> = new Map(); // 管理多个连接
	private static isNetworkConnected : boolean = true;

	/**
	 * 构造函数，支持传入自定义配置
	 * @param url WebSocket 连接地址
	 * @param options 可选的配置项
	 */
	constructor(url : string, options ?: Partial<UniApp.ConnectSocketOption>) {
		this.url = url;

		this.options = {
			url, // 确保 URL 设置
			header: options?.header || {}, // 默认空的 header
			method: options?.method || 'GET', // 默认 GET 方法
			...options, // 合并传入的其他配置项
		};

		this.setupNetworkListener(); // 设置网络状态监听
	}

	/**
	 * 获取或创建一个 WebSocket 连接
	 */
	static getInstance(url : string, options ?: Partial<UniApp.ConnectSocketOption>) : WebSocketManager {
		if (!WebSocketManager.connections.has(url)) {
			WebSocketManager.connections.set(url, new WebSocketManager(url, options));
		}
		return WebSocketManager.connections.get(url)!;
	}

	/**
	 * 连接 WebSocket
	 */
	connect() : void {
		if (this.isConnected || !WebSocketManager.isNetworkConnected) {
			return;
		}

		this.socketTask = uni.connectSocket({
			...this.options,
			success: () => {
				console.log(`[WebSocket] 连接成功: ${this.url}`);
			},
			fail: (error) => {
				console.error(`[WebSocket] 连接失败: ${this.url}`, error);
				this.reconnect();
			},
		});

		this.setupEventListeners();
	}

	/**
	 * 发送消息
	 */
	send(message : string) : void {
		if (this.isConnected && this.socketTask) {
			this.socketTask.send({
				data: message,
				success: () => {
					console.log(`[WebSocket] 消息发送成功: ${message}`);
				},
				fail: (error) => {
					console.error(`[WebSocket] 消息发送失败`, error);
				},
			});
		} else {
			console.warn(`[WebSocket] 当前未连接，消息未发送: ${message}`);
		}
	}

	/**
	 * 手动关闭连接
	 */
	close() : void {
		if (this.socketTask) {
			this.socketTask.close({
				success: () => {
					console.log(`[WebSocket] 连接已关闭: ${this.url}`);
					this.cleanup();
				},
				fail: (error) => {
					console.error(`[WebSocket] 关闭失败: ${this.url}`, error);
				},
			});
		}
	}

	/**
	 * 注册事件监听器
	 */
	onOpen(callback : () => void) : void {
		this.onOpenCallbacks.push(callback);
	}

	onClose(callback : () => void) : void {
		this.onCloseCallbacks.push(callback);
	}

	onError(callback : (error : any) => void) : void {
		this.onErrorCallbacks.push(callback);
	}

	onMessage(callback : (message : any) => void) : void {
		this.onMessageCallbacks.push(callback);
	}

	/**
	 * 设置事件监听器
	 */
	private setupEventListeners() : void {
		if (!this.socketTask) return;

		this.socketTask.onOpen(() => {
			this.isConnected = true;
			this.reconnectAttempts = 0;
			this.startHeartbeat();
			this.executeCallbacks(this.onOpenCallbacks);
			console.log(`[WebSocket] 已连接: ${this.url}`);
		});

		this.socketTask.onClose(() => {
			this.isConnected = false;
			this.stopHeartbeat();
			this.executeCallbacks(this.onCloseCallbacks);
			console.warn(`[WebSocket] 连接关闭: ${this.url}`);
			this.reconnect();
		});

		this.socketTask.onError((error) => {
			this.isConnected = false;
			this.executeCallbacks(this.onErrorCallbacks, error);
			console.error(`[WebSocket] 连接错误: ${this.url}`, error);
			this.reconnect();
		});

		this.socketTask.onMessage((message) => {
			this.executeCallbacks(this.onMessageCallbacks, message);
			if (message.data === 'pong') {
				this.resetHeartbeat();
			}
		});
	}

	/**
	 * 心跳检测
	 */
	private startHeartbeat() : void {
		timer.addTimer(timerName.ws, () => {
			if (this.isConnected) {
				this.send('ping');
			}
		}, this.heartbeatInterval);
	}

	private resetHeartbeat() : void {
		timer.clearTimer(timerName.ws);
		this.startHeartbeat();
	}

	private stopHeartbeat() : void {
		timer.clearTimer(timerName.ws);
	}

	/**
	 * 自动重连
	 */
	private reconnect() : void {
		if (this.reconnectAttempts >= this.maxReconnectAttempts) {
			console.error(`[WebSocket] 重连次数已达上限: ${this.url}`);
			return;
		}

		this.reconnectAttempts++;
		this.reconnectTimer = setTimeout(() => {
			console.log(`[WebSocket] 尝试重连(${this.reconnectAttempts}/${this.maxReconnectAttempts}): ${this.url}`);
			this.connect();
		}, this.reconnectInterval);
	}

	/**
	 * 清理资源
	 */
	private cleanup() : void {
		this.stopHeartbeat();
		if (this.reconnectTimer) {
			clearTimeout(this.reconnectTimer);
		}
	}

	/**
	 * 执行回调函数
	 */
	private executeCallbacks(callbacks : Array<(...args : any[]) => void>, ...args : any[]) : void {
		callbacks.forEach((callback) => {
			callback(...args);
		});
	}

	/**
	 * 设置网络状态监听
	 */
	private setupNetworkListener() : void {
		uni.onNetworkStatusChange((res) => {
			WebSocketManager.isNetworkConnected = res.isConnected;
			if (res.isConnected) {
				console.log(`[WebSocket] 网络已连接: ${this.url}`);
				this.connect();
			} else {
				console.warn(`[WebSocket] 网络断开: ${this.url}`);
				this.cleanup();
			}
		});
	}
}

export default WebSocketManager;