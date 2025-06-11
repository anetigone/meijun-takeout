import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import DashboardAdm from '../views/Dashboard-adm.vue'; // 可以自行创建
import DashboardMer from "../views/Dashboard-mer.vue";
import DashboardEmp from "../views/Dashboard-emp.vue";
import Index from "../views/index.vue";

const routes = [
  {
    path: '/login',
    component: Login
  },
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/index',
    component: Index
  },
  {
    path: '/adm-dashboard',
    component: DashboardAdm,
    meta: { requiresAuth: true }
  },
  {
    path: '/mer-dashboard',
    component: DashboardMer,
    meta: { requiresAuth: true }
  },
  {
    path: '/emp-dashboard',
    component: DashboardEmp,
    meta: { requiresAuth: true }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
