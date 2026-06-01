<template>
  <el-row :gutter="20">
    <el-col :span="4" v-for="item in stats" :key="item.label">
      <el-card>
        <div>{{ item.label }}</div>
        <div style="font-size: 32px; font-weight: bold;">{{ item.value }}</div>
      </el-card>
    </el-col>
  </el-row>
  <el-card style="margin-top: 20px; height: calc(100vh - 200px);">
    <div style="padding: 20px;">
      <div style="font-size: 18px; font-weight: bold; margin-bottom: 15px;">养老院</div>
      <img src="/src/assets/yanglaoyuan.png" style="width: 100%; height: calc(100% - 60px); object-fit: cover; border-radius: 8px;" />
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const stats = ref([
  { label: '用户数量', value: 0 },
  { label: '老人数量', value: 0 },
  { label: '服务数量', value: 0 },
  { label: '房型数量', value: 0 },
  { label: '今日订单数量', value: 0 },
  { label: '今日预约数量', value: 0 }
])

function fetchHomeData() {
  axios.get('/admin/home/data').then(res => {
    if (res.data.code === 200) {
      const data = res.data.data
      stats.value = [
        { label: '用户数量', value: data.userCount },
        { label: '老人数量', value: data.elderCount },
        { label: '服务数量', value: data.projectCount },
        { label: '房型数量', value: data.roomCount },
        { label: '今日订单数量', value: data.orderCount },
        { label: '今日预约数量', value: data.reservationCount }
      ]
    }
  })
}

onMounted(fetchHomeData)
</script> 