import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {//获取路径中包含了/api的请求
        // target: 'http://localhost:8080',//后台服务所在的源, iotback 为docker-compose中的服务名,开发测试为localhost
        target: 'http://iotback:8080',//后台服务所在的源, iotback 为docker-compose中的服务名,开发测试为localhost
        changeOrigin: true,//修改源
        rewrite: (path) => path.replace(/^\/api/, '')// /api替换为''
      }
    }
  }
})
