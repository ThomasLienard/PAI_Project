// Ce composant affiche la liste des commandes à préparer.
// Rôle :
// - Récupérer les commandes depuis le backend via une API.
// - Afficher les commandes avec leur statut (ex. : "En cours", "Prêt").
// - Permettre au cuisinier de marquer une commande comme terminée.

<template>
  <div>
    <NavBar />
    <div class="order-list">
      <h3>Commandes à préparer</h3>
      <div v-if="orders.length === 0" class="empty-state">
        Aucune commande à préparer pour le moment.
      </div>
      <div class="orders-grid">
        <div
          v-for="order in sortedOrders"
          :key="order.id"
          class="order-card"
        >
          <div class="order-header">
            <span class="order-number">Commande #{{ order.id }}</span>
            <span class="table-number">Table {{ order.tableNumber }}</span>
            <span class="order-time">{{ formatTime(order.createdAt) }}</span>
          </div>
          <div class="order-status">
            <span class="item-status" :class="order.status">{{ order.status }}</span>
          </div>
          <div class="order-items">
            <div
              v-for="item in order.items"
              :key="item.id"
              class="order-item"
            >
              <span class="item-name">{{ item.name }}</span>
              <span class="item-qty">x{{ item.quantity }}</span>
              <span v-if="item.specialInstructions" class="item-instructions">
                <i class="fas fa-info-circle"></i> {{ item.specialInstructions }}
              </span>
            </div>
          </div>
          <div class="order-actions">
            <button
              v-if="order.status === 'en_attente'"
              @click="markOrderInPreparation(order.id)"
              class="btn-secondary"
            >
              Passer en préparation
            </button>
            <button
              v-if="order.status === 'en préparation'"
              @click="markOrderReady(order.id)"
              class="btn-success"
            >
              Marquer comme prêt à servir
            </button>
            <button
              v-if="order.status === 'prête'"
              @click="markOrderCompleted(order.id)"
              class="btn-complete"
            >
              Marquer la commande comme terminée
            </button>
          </div>
        </div>
      </div>
    </div>
    <audio ref="audioRef" src="/sounds/new-order.mp3" preload="auto"></audio>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import NavBar from '../NavBar.vue'
import apiClient from '@/services/apiClient'

const orders = ref<any[]>([])
const audioRef = ref<HTMLAudioElement | null>(null)
let lastOrderIds: number[] = []

// Récupérer les commandes depuis le backend
const fetchOrders = async () => {
  const res = await apiClient.get('/cuisinier/orders')
  orders.value = res.data.sort((a: { createdAt: string }, b: { createdAt: string }) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime())
}

// Rafraîchissement périodique (polling)
const pollOrders = async () => {
  await fetchOrders()
  const currentIds = orders.value.map(o => o.id)
  const newIds = currentIds.filter(id => !lastOrderIds.includes(id))
  if (newIds.length > 0 && audioRef.value) {
    audioRef.value.play()
  }
  lastOrderIds = currentIds
  setTimeout(pollOrders, 4000)
}

const formatTime = (iso: string) => {
  if (!iso) return ''
  const d = new Date(iso)
  return d.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
}

const sortedOrders = computed(() => orders.value)

// Actions sur la commande
const markOrderInPreparation = async (orderId: number) => {
  await apiClient.patch(`/cuisinier/orders/${orderId}/in-preparation`)
  await fetchOrders()
}
const markOrderReady = async (orderId: number) => {
  await apiClient.patch(`/cuisinier/orders/${orderId}/ready`)
  await fetchOrders()
}
const markOrderCompleted = async (orderId: number) => {
  await apiClient.patch(`/cuisinier/orders/${orderId}/complete`)
  await fetchOrders()
}

onMounted(() => {
  pollOrders()
})
</script>

<style scoped>
.order-list {
  padding: 2rem;
}
.orders-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
}
.order-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px #0001;
  padding: 1.5rem;
  min-width: 350px;
  flex: 1 1 350px;
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.order-number {
  font-weight: bold;
  color: #2196f3;
}
.table-number {
  font-size: 1rem;
  color: #666;
}
.order-time {
  font-size: 0.9rem;
  color: #aaa;
}
.order-status {
  margin-bottom: 0.5rem;
}
.item-status {
  font-size: 0.9em;
  padding: 2px 8px;
  border-radius: 4px;
  background: #eee;
  text-transform: capitalize;
}
.item-status.en_attente { background: #ffe082; }
.item-status.en\ préparation { background: #90caf9; }
.item-status.prête { background: #a5d6a7; }
.item-status.terminée { background: #bdbdbd; }
.order-items {
  margin-bottom: 1rem;
}
.order-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.5rem;
}
.item-instructions {
  font-style: italic;
  color: #e65100;
  margin-left: 1rem;
}
.order-actions {
  margin-top: 1rem;
}
.btn-secondary, .btn-success, .btn-complete {
  margin-right: 0.5rem;
  padding: 0.4rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.btn-secondary { background: #90caf9; color: #222; }
.btn-success { background: #a5d6a7; color: #222; }
.btn-complete { background: #2196f3; color: #fff; }
.empty-state {
  color: #888;
  font-style: italic;
  margin: 2rem 0;
}
</style>