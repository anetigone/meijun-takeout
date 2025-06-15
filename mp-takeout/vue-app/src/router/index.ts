import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import DashboardAdm from '../views/Dashboard-adm.vue';
import DashboardMer from "../views/Dashboard-mer.vue";
import DashboardEmp from "../views/Dashboard-emp.vue";
import Chat from "../views/Chat.vue";
import Index from "../views/index.vue";
import NotFound from '../views/NotFound.vue';
import Game from "../views/Game.vue";
import HelpCenter from "../views/HelpCenter.vue";
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
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound
  },
  {
    path: '/game',
    component: Game,
    meta:
    {
      requiresAuth: false,
      title: 'Game'
    }
  },
  {
    path: '/help',
    component: HelpCenter,
    meta:
    {
      requiresAuth: false,
      title: '帮助中心'
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
