import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types/user'
import { login, register, logout } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const currentUser = ref<User | null>(null)
  const token = ref<string | null>(null)

  const isLoggedIn = computed(() => !!token.value)

  async function loginUser(username: string, password: string) {
    const response = await login(username, password)
    token.value = response.data.token
    currentUser.value = response.data.user
    localStorage.setItem('token', token.value)
  }

  async function logoutUser() {
    await logout()
    token.value = null
    currentUser.value = null
    localStorage.removeItem('token')
  }

  function initializeFromStorage() {
    const storedToken = localStorage.getItem('token')
    if (storedToken) {
      token.value = storedToken
      // TODO: 获取用户信息
    }
  }

  return {
    currentUser,
    token,
    isLoggedIn,
    login: loginUser,
    logout: logoutUser,
    initializeFromStorage
  }
}) 