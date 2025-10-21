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
    server: {
      host: '0.0.0.0',
      port: 5173,
      open: true,
      proxy: {
        '/api': {
          target: env.VITE_API_BASE_URL || 'http://10.116.40.104:8081',
          changeOrigin: true,
          secure: false,
          timeout: 10000
        },
        '/files': {
          target: env.VITE_API_BASE_URL || 'http://10.116.40.104:8081',
          changeOrigin: true,
          secure: false,
          timeout: 10000,
          rewrite: (path) => path.replace(/^\/files/, '/api/files')
        }
      }
    }
  }
})