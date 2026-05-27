import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/warehouse' },
  { path: '/warehouse', component: () => import('../views/WarehouseManage.vue') },
  { path: '/category', component: () => import('../views/CategoryManage.vue') },
  { path: '/material', component: () => import('../views/MaterialManage.vue') },
  { path: '/inbound', component: () => import('../views/InboundManage.vue') },
  { path: '/outbound', component: () => import('../views/OutboundManage.vue') },
  { path: '/inventory', component: () => import('../views/InventoryQuery.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
