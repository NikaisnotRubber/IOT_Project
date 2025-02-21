import { createRouter, createWebHistory } from 'vue-router'
import LayoutVue from '@/views/Layout.vue'
import DashboardVue from '@/views/visual/Dashboard.vue'
import MessageMapVue from '@/views/visual/MessageMap.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'
import DeviceCategoryVue from '@/views/device/DeviceCategory.vue'
import DeviceManageVue from '@/views/device/DeviceManage.vue'
import LoginVue from '@/views/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'main',
      component: LayoutVue,
      redirect: '/dashboard',
      children:[
        {path:'/dashboard',name:'dashboard',component: DashboardVue },
        {path:'msgmap',name:'msgmap',component: MessageMapVue },
        {path:'/user/info',name:'userInfo',component: UserInfoVue },
        {path:'/user/resetPassword',name:'resetPassword',component: UserResetPasswordVue },
        {path:'/category',name:'category',component: DeviceCategoryVue },
        {path:'/device',name:'device',component: DeviceManageVue },
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginVue
    }
  ]
})

export default router
