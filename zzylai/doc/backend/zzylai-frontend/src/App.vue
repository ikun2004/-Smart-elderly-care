<script setup>
import HelloWorld from './components/HelloWorld.vue'
import { House, User, Avatar, List, OfficeBuilding, Suitcase, Calendar, Document, Watch, Cpu } from '@element-plus/icons-vue'
</script>

<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #232b3b; color: #fff;">
      <div style="font-size: 26px; font-weight: bold; text-align: center; margin: 30px 0;">中州养老</div>
      <el-menu :default-active="$route.path" router background-color="#232b3b" text-color="#fff" active-text-color="#409EFF" style="border-right: none">
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          首页
        </el-menu-item>
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          用户管理
        </el-menu-item>
        <el-menu-item index="/elder">
          <el-icon><Avatar /></el-icon>
          老人管理
        </el-menu-item>
        <el-menu-item index="/order">
          <el-icon><List /></el-icon>
          订单管理
        </el-menu-item>
        <el-menu-item index="/room">
          <el-icon><OfficeBuilding /></el-icon>
          房型管理
        </el-menu-item>
        <el-menu-item index="/service">
          <el-icon><Suitcase /></el-icon>
          护理服务管理
        </el-menu-item>
        <el-menu-item index="/health">
          <el-icon><Document /></el-icon>
          体检报告
        </el-menu-item>
        <el-menu-item index="/reserve">
          <el-icon><Calendar /></el-icon>
          预约管理
        </el-menu-item>
        <el-menu-item index="/wearable">
          <el-icon><Watch /></el-icon>
          穿戴设备
        </el-menu-item>
        <el-menu-item index="/iot">
          <el-icon><Cpu /></el-icon>
          IOT数据
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-main style="background: #f5f6fa;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
body {
  margin: 0;
}
</style>
