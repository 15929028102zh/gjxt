import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'
import { isTokenValid } from '@/utils/token'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    meta: { title: '首页', icon: 'House' },
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/index.vue'),
        meta: { title: '首页', icon: 'House' }
      }
    ]
  },
  {
    path: '/inspection',
    component: Layout,
    redirect: '/inspection/weekly',
    meta: { title: '周检管理', icon: 'DocumentChecked' },
    children: [
      {
        path: 'weekly',
        name: 'WeeklyInspection',
        component: () => import('@/views/inspection/weekly/index.vue'),
        meta: { title: '周检记录录入', icon: 'EditPen' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统设置', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'dept',
        name: 'Dept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'dict',
        name: 'Dict',
        component: () => import('@/views/system/dict/index.vue'),
        meta: { title: '字典管理', icon: 'Collection' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 工业巡检系统`
  }
  
  // 登录验证 - 检查token是否有效且未过期
  if (to.path !== '/login' && !isTokenValid()) {
    next('/login')
  } else {
    next()
  }
})

export default router