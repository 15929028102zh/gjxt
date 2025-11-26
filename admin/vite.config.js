import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig(({ mode }) => {
  // 加载环境变量
  const env = loadEnv(mode, process.cwd(), '')
  
  return {
    plugins: [vue()],
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src')
      }
    },
    define: {
      __VUE_PROD_DEVTOOLS__: false,
    },
    optimizeDeps: {
      include: ['sockjs-client', '@stomp/stompjs']
    },
    server: {
      host: '0.0.0.0',
      port: 5173,
      open: true,
      proxy: {
        '/api': {
          target: env.VITE_API_BASE_URL || 'http://localhost:8081',
          changeOrigin: true,
          secure: false,
          // 支持WebSocket代理，避免SockJS在开发环境握手失败
          ws: true,
          // 提高超时时间以兼容XHR流式/长轮询
          timeout: 60000,
          proxyTimeout: 60000
        },
        '/files': {
          target: env.VITE_API_BASE_URL || 'http://localhost:8081',
          changeOrigin: true,
          secure: false,
          timeout: 60000,
          proxyTimeout: 60000,
          rewrite: (path) => path.replace(/^\/files/, '/api/files')
        }
      }
    }
  }
})