import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import AdminView from '../views/admin/AdminView.vue';
import ConnectView from '../views/ConnectView.vue';
import RegisterView from '../views/RegisterView.vue';
import UserManagement from '../views/admin/UserManagement.vue';
import MenuManagement from '../views/admin/MenuManagement.vue';
import StatisticsManagement from '../views/admin/StatisticsManagement.vue';
import SuppliersManagement from '../views/admin/SuppliersManagement.vue';
import UserView from '../views/UserView.vue';
import FeedbackView from '../views/FeedbackView.vue';
import ModifyView from '../views/ModifyView.vue';
import CommandeView from '../views/CommandeView.vue';
import CommanderView from '../views/CommanderView.vue';
import ChefView from '../views/ChefView.vue';
import OrderList from '../components/chef/OrderList.vue';
import RecipeManager from '../components/chef/RecipeManager.vue';
import ServerView from '../views/server/ServerView.vue';
import ServerOrdersListView from '../views/server/ServerOrdersListView.vue';import ServerReservationsView from '../views/server/ServerReservationsView.vue';
import ServerNewOrderView from '../views/server/ServerNewOrderView.vue';
import ReservationView from '../views/ReservationView.vue'
import MenuView from '../views/MenuView.vue';
import StockAdminView from '../views/admin/StockAdminView.vue';
import SupplierOrderView from '../views/admin/SupplierOrderView.vue';


const routes = [
  { path: '/', name: 'home', component: Home },
  { path: '/login', name: 'login', component: ConnectView },
  { path: '/register', name: 'register', component: RegisterView },
  { path: '/admin', name: 'admin', component: AdminView, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/user', name: 'admin-user', component: UserManagement, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/menus',name: 'admin-menus',component: MenuManagement,meta: { requiresAuth: true, role: 'ADMIN' } },
  {path: '/admin/statistics',name: 'admin-statistics',component: StatisticsManagement, meta: { requiresAuth: true, role: 'ADMIN' }},
  {path: '/admin/suppliers',name: 'admin-suppliers',component: SuppliersManagement, meta: { requiresAuth: true, role: 'ADMIN' }},
  {path: '/user',name: 'user',component: UserView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/feedback',name: 'feedback',component: FeedbackView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/modifier',name: 'modifier',component: ModifyView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/commandes',name: 'commandes',component: CommandeView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/commander',name: 'commander',component: CommanderView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/user/menu',name: 'Menu',component: MenuView, meta: { requiresAuth: true, role: 'CLIENT' }},
  {path: '/chef/',name: 'chef',component: ChefView, meta: { requiresAuth: true, role: 'CUISINIER' }},
  {path: '/chef/orders',name: 'ChefOrders',component: OrderList, meta: { requiresAuth: true, role: 'CUISINIER' }},
  {path: '/chef/recipes',name: 'chefRecipe',component: RecipeManager, meta: { requiresAuth: true, role: 'CUISINIER' }},
  {path: '/server', name: 'server',component: ServerView, meta: { requiresAuth: true, role: 'SERVEUR' }},
  {path: '/server/orders', name: 'server-orders', component: ServerOrdersListView, meta: { requiresAuth: true, role: 'SERVEUR' } },
  {path: '/server/reservations', name: 'server-reservations', component: ServerReservationsView ,meta: { requiresAuth: true, role: 'SERVEUR' }},
  {path: '/server/new-order', name: 'server-new-order',component: ServerNewOrderView, meta: { requiresAuth: true, role: 'SERVEUR' }},
  {path: '/user/reservation',name: 'reservation',component: ReservationView ,meta: { requiresAuth: true, role: 'CLIENT' }},
  { path: '/admin/stocks', name: 'admin-stocks', component: StockAdminView, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/admin/orders', name: 'admin-supplier-orders', component: SupplierOrderView, meta: { requiresAuth: true, role: 'ADMIN' } },
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
