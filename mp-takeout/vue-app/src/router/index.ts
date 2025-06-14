import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import DashboardAdm from '../views/Dashboard-adm.vue'; // 可以自行创建
import DashboardMer from "../views/Dashboard-mer.vue";
import DashboardEmp from "../views/Dashboard-emp.vue";
import Chat from "../views/Chat.vue"; // 可以自行创建
import Index from "../views/index.vue";
import { checkAuthExpired } from '../utils/authStorage';
import { ElMessage } from 'element-plus';

const routes = [
  {
    path: '/login',
    component: Login,
    meta: { title: '登录' }
  },
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/index',
    component: Index,
    meta: { title: '首页' }
  },
  {
    path: '/adm-dashboard',
    component: DashboardAdm,
    meta: 
    { 
      requiresAuth: true, 
      title: '管理员面板' 
    }
  },
  {
    path: '/mer-dashboard',
    component: DashboardMer,
    meta: 
    { 
      requiresAuth: true ,
      title: '商家面板'
    }
  },
  {
    path: '/emp-dashboard',
    component: DashboardEmp,
    meta: 
    { 
      requiresAuth: true ,
      title: '员工面板'
    }
  },
  {
    path: '/chat',
    component: Chat,
    meta: 
    {
      requiresAuth: true,
      title: '聊天室'
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

const path = '/login';

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title as string || '默认标题';
  const token = localStorage.getItem('token');
  if(to.meta.requiresAuth && !token) {
    ElMessage.error('请先登录');
    next();
    return;
  }
  const checkAuth = checkAuthExpired();
  if (to.meta.requiresAuth && !checkAuth) {
    next();
    return;
  }
  
  next();
});

export default router;
