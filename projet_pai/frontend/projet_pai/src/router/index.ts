import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import AdminView from '../views/AdminView.vue';
import ConnectView from '../views/ConnectView.vue';
import RegisterView from '../views/RegisterView.vue';
import UserManagement from '../views/UserManagement.vue';
import MenuManagement from '../views/MenuManagement.vue';
import OrderManagement from '../views/OrderManagement.vue';
import StatisticsManagement from '../views/StatisticsManagement.vue';
import UserView from '../views/UserView.vue';
import ReservationView from '../views/ReservationView.vue';
import ReservationChoiceView from '../views/ReservationChoiceView.vue';
import FeedbackView from '../views/FeedbackView.vue';
import ModifyView from '../views/ModifyView.vue';
import CommandeView from '../views/CommandeView.vue';
import CommanderView from '../views/CommanderView.vue';
import ReservationsView from '../views/ReservationsView.vue';
import ChefView from '../views/ChefView.vue';
import OrderList from '../components/chef/OrderList.vue';
import RecipeManager from '../components/chef/RecipeManager.vue';


const routes = [
  { path: '/', name: 'home', component: Home },
  { path: '/login', name: 'login', component: ConnectView },
  { path: '/register', name: 'register', component: RegisterView },
  { path: '/admin', name: 'admin', component: AdminView, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/user', name: 'admin-user', component: UserManagement, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/menus',name: 'admin-menus',component: MenuManagement,meta: { requiresAuth: true, role: 'ADMIN' } },
  {path: '/admin/orders',name: 'admin-orders',component: OrderManagement, meta: { requiresAuth: true, role: 'ADMIN' }},
  {path: '/admin/statistics',name: 'admin-statistics',component: StatisticsManagement, meta: { requiresAuth: true, role: 'ADMIN' }},
  {path: '/user',name: 'user',component: UserView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/reservation',name: 'reservation',component: ReservationView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/reservation/choix',name: 'reservationChoix',component: ReservationChoiceView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/feedback',name: 'feedback',component: FeedbackView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/modifier',name: 'modifier',component: ModifyView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/commandes',name: 'commandes',component: CommandeView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/commander',name: 'commander',component: CommanderView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/reservations',name: 'mesReservations',component: ReservationsView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/chef/',name: 'chef',component: ChefView, meta: { requiresAuth: true, role: 'CUISINIER' }},
  {path: '/chef/orders',name: 'ChefOrders',component: OrderList, meta: { requiresAuth: true, role: 'CUISINIER' }},
  {path: '/chef/recipes',name: 'chef',component: RecipeManager, meta: { requiresAuth: true, role: 'CUISINIER' }},
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
