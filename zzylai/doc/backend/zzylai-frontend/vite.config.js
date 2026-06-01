import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/admin': {
        target: 'http://localhost:9995',
        changeOrigin: true,
        rewrite: path => path,
      },
    },
  },
})
