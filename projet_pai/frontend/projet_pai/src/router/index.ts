import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import AdminView from '../views/AdminView.vue';
import ConnectView from '../views/ConnectView.vue';
import RegisterView from '../views/RegisterView.vue';
import UserManagement from '../views/UserManagement.vue';
import MenuManagement from '../views/MenuManagement.vue';
import OrderManagement from '../views/OrderManagement.vue';
import StatisticsManagement from '../views/StatisticsManagement.vue';

const routes = [
  { path: '/', name: 'home', component: Home },
  { path: '/login', name: 'login', component: ConnectView },
  { path: '/register', name: 'register', component: RegisterView },
  { path: '/admin', name: 'admin', component: AdminView, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/user', name: 'admin-user', component: UserManagement, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/menus',name: 'admin-menus',component: MenuManagement,meta: { requiresAuth: true, role: 'ADMIN' } },
  {path: '/admin/orders',name: 'admin-orders',component: OrderManagement, meta: { requiresAuth: true, role: 'ADMIN' }},
  {path: '/admin/statistics',name: 'admin-statistics',component: StatisticsManagement, meta: { requiresAuth: true, role: 'ADMIN' }},
];

const router = createRouter({
  history: createWebHistory(''),
  routes,
});

router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('jwtToken');
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      next({ name: 'login' });
    } else {
      const userRole = JSON.parse(atob(token.split('.')[1])).role;
      if (to.meta.role && to.meta.role !== userRole) {
        next({ name: 'login' });
      } else {
        next();
      }
    }
  } else {
    next();
  }
});

export default router;
