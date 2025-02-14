import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: 'photos',
        name: 'photos',
        component: () => import('@/views/photos/PhotoList.vue')
      },
      {
        path: 'albums',
        name: 'albums',
        component: () => import('@/views/albums/AlbumList.vue')
      },
      {
        path: 'albums/:id',
        name: 'album-detail',
        component: () => import('@/views/albums/AlbumDetail.vue')
      },
      {
        path: 'timeline',
        name: 'timeline',
        component: () => import('@/views/Timeline.vue')
      },
      {
        path: 'map',
        name: 'map',
        component: () => import('@/views/MapView.vue')
      }
    ]
  },
  {
    path: '/auth',
    component: () => import('@/layouts/AuthLayout.vue'),
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('@/views/auth/Login.vue')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import('@/views/auth/Register.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 