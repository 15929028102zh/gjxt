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
    path: '/monthly',
    component: Layout,
    redirect: '/monthly/task',
    meta: { title: '月检管理', icon: 'Calendar' },
    children: [
      {
        path: 'task',
        name: 'MonthlyTask',
        component: () => import('@/views/monthly/task/index.vue'),
        meta: { title: '月检任务', icon: 'Document' }
      },
      {
        path: 'gjbz',
        name: 'GjbzData',
        component: () => import('@/views/monthly/gjbz/index.vue'),
        meta: { title: '岗检标准', icon: 'DataBoard' }
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
  },
  {
    path: '/profile',
    component: Layout,
    meta: { hideInMenu: true },
    children: [
      {
        path: '',
        name: 'Profile',
        component: () => import('@/views/profile/index.vue'),
        meta: { title: '个人中心', icon: 'User' }
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
  console.log('路由守卫触发: 从', from.path, '到', to.path)
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  
  // 登录验证 - 检查token是否有效且未过期
  const tokenValid = isTokenValid()
  console.log('Token验证结果:', tokenValid)
  
  if (to.path !== '/login' && !tokenValid) {
    console.log('Token无效，重定向到登录页')
    next('/login')
  } else {
    console.log('允许导航到:', to.path)
    next()
  }
})

export default router