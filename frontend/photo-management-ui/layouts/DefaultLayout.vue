<template>
  <el-container class="layout-container">
    <el-header>
      <nav class="nav-header">
        <div class="logo">
          Photo Management
        </div>
        <el-menu
          mode="horizontal"
          router
          :ellipsis="false"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/photos">照片</el-menu-item>
          <el-menu-item index="/albums">相册</el-menu-item>
          <el-menu-item index="/timeline">时间线</el-menu-item>
          <el-menu-item index="/map">地图</el-menu-item>
        </el-menu>
        <div class="user-menu">
          <el-dropdown v-if="userStore.isLoggedIn">
            <el-avatar :size="32" :src="userStore.avatar" />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProfile">个人信息</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <template v-else>
            <el-button type="primary" @click="$router.push('/auth/login')">
              登录
            </el-button>
          </template>
        </div>
      </nav>
    </el-header>
    
    <el-main>
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const handleProfile = () => {
  // TODO: 实现个人信息页面
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/auth/login')
}
</script>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
}

.nav-header {
  display: flex;
  align-items: center;
  height: 100%;
  
  .logo {
    font-size: 1.5rem;
    font-weight: bold;
    margin-right: 2rem;
  }
  
  .user-menu {
    margin-left: auto;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 