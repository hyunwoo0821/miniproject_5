import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": {
        target: "http://backend-service:8080",
        changeOrigin: true,
      },
      "/auth": {
        target: "http://backend-service:8080",
        changeOrigin: true,
      },
    },
  },
})