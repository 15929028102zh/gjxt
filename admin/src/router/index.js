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
    redirect: 'home',
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
      },
      {
        path: 'submit',
        name: 'WeeklySubmit',
        component: () => import('@/views/inspection/submit/index.vue'),
        meta: { title: '周检提交', icon: 'Upload' }
      },
      {
        path: 'delayed',
        name: 'DelayedRectification',
        component: () => import('@/views/inspection/delayed/index.vue'),
        meta: { title: '延迟整改', icon: 'Warning' }
      },
      {
        path: 'matrix',
        name: 'DepartmentProfessionMatrix',
        component: () => import('@/views/inspection/matrix/index.vue'),
        meta: { title: '周检数据汇总', icon: 'Grid' }
      }
      ,
      {
        path: 'nature',
        name: 'InspectionNature',
        component: () => import('@/views/inspection/nature/index.vue'),
        meta: { title: '周检问题属性统计', icon: 'DataAnalysis' }
      },
      {
        path: 'audit',
        name: 'InspectionAudit',
        component: () => import('@/views/inspection/audit/index.vue'),
        meta: { title: '待审核记录', icon: 'DocumentChecked' }
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
        meta: { title: '月检任务', icon: 'Document', requiresMonthlyAdmin: true }
      },
      {
        path: 'record',
        name: 'MonthlyRecord',
        component: () => import('@/views/inspection/monthly/index.vue'),
        meta: { title: '月检记录录入', icon: 'EditPen' }
      },
      {
        path: 'submit',
        name: 'MonthlySubmit',
        component: () => import('@/views/inspection/monthlySubmit/index.vue'),
        meta: { title: '月检提交', icon: 'Upload' }
      },
      {
        path: 'gjbz',
        name: 'GjbzData',
        component: () => import('@/views/monthly/gjbz/index.vue'),
        meta: { title: '岗检标准', icon: 'DataBoard', requiresMonthlyAdmin: true }
      }
      ,
      {
        path: 'delayed',
        name: 'MonthlyDelayed',
        component: () => import('@/views/monthly/delayed/index.vue'),
        meta: { title: '延迟整改', icon: 'Warning' }
      },
      {
        path: 'audit',
        name: 'MonthlyAudit',
        component: () => import('@/views/monthly/audit/index.vue'),
        meta: { title: '待审核记录', icon: 'DocumentChecked', requiresMonthlyAdmin: true }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统设置', icon: 'Setting', requiresAdmin: true },
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', requiresAdmin: true }
      },
      {
        path: 'dept',
        name: 'Dept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding', requiresAdmin: true }
      },
      {
        path: 'dict',
        name: 'Dict',
        component: () => import('@/views/system/dict/index.vue'),
        meta: { title: '字典管理', icon: 'Collection', requiresAdmin: true }
      },
      {
        path: 'notification',
        name: 'NotificationManagement',
        component: () => import('@/views/system/notification/index.vue'),
        meta: { title: '通知管理', icon: 'Bell', requiresAdmin: true }
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

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 榆林公司岗检系统}` : '榆林公司岗检系统'

  // 检查是否需要登录
  if (to.path !== '/login') {
    if (!isTokenValid()) {
      next('/login')
      return
    }

    let userInfo = null
    try {
      const userInfoStr = localStorage.getItem('userInfo')
      if (userInfoStr) userInfo = JSON.parse(userInfoStr)
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }

    if (!userInfo) {
      next('/')
      return
    }

    // 兼容老 isAdmin, 任一管理员标志位为 1 都视为管理员
    const isLegacyAdmin = userInfo.isAdmin === 1
    const isWeeklyAdmin = userInfo.isWeeklyAdmin === 1
    const isMonthlyAdmin = userInfo.isMonthlyAdmin === 1
    const isAnyAdmin = isLegacyAdmin || isWeeklyAdmin || isMonthlyAdmin

    // 共享页面:任一管理员即可
    if (to.meta?.requiresAdmin && !isAnyAdmin) {
      next('/')
      return
    }

    // 周检专属:要求周检管理员(或兼容老 admin)
    if (to.meta?.requiresWeeklyAdmin && !(isLegacyAdmin || isWeeklyAdmin)) {
      next('/')
      return
    }

    // 月检专属:要求月检管理员(或兼容老 admin)
    if (to.meta?.requiresMonthlyAdmin && !(isLegacyAdmin || isMonthlyAdmin)) {
      next('/')
      return
    }
  }

  next()
})

export default router