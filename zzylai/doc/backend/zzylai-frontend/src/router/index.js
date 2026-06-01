import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', component: () => import('../views/Home.vue') },
  { path: '/user', component: () => import('../views/UserManage.vue') },
  { path: '/elder', component: () => import('../views/ElderManage.vue') },
  { path: '/order', component: () => import('../views/OrderManage.vue') },
  { path: '/room', component: () => import('../views/RoomManage.vue') },
  { path: '/service', component: () => import('../views/ServiceManage.vue') },
  { path: '/reserve', component: () => import('../views/ReserveManage.vue') },
  { path: '/health', component: () => import('../views/HealthAssessment.vue') },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/wearable', component: () => import('../views/WearableDevice.vue') },
  { path: '/iot', component: () => import('../views/IotData.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 