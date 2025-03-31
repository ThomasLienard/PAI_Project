<template>
  
  <div class="current-orders">
    <div class="orders-grid">
      <div v-for="order in sortedOrders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-number">Commande #{{ order.id }}</span>
          <span :class="['status', order.status]">{{ order.status }}</span>
        </div>
        <div class="order-info">
          <p v-if="order.clientName">Client: {{ order.clientName }}</p>
          <p>Table: {{ order.tableNumber }}</p>
          <p>Heure: {{ formatTime(order.orderTime) }}</p>
        </div>
        <div class="order-items">
          <h4>Articles commandés:</h4>
          <ul>
            <li v-for="item in order.items" :key="item.id">
              {{ item.quantity }}x {{ item.name }}
            </li>
          </ul>
        </div>
        <button 
          v-if="order.status === 'en préparation'" 
          @click="$emit('cancelOrder', order.id)"
          class="cancel-btn"
        >
          Annuler la commande
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CurrentOrders',
  props: {
    orders: {
      type: Array,
      required: false, // Changed from true to false
      default: () => [] // Ajout d'une valeur par défaut
    }
  },
  computed: {
    sortedOrders() {
      // Vérification que this.orders existe et est un tableau
      if (!Array.isArray(this.orders)) {
        return []
      }
      
      return [...this.orders].sort((a, b) => {
        // Trier d'abord par statut
        const statusOrder = {
          'en préparation': 1,
          'prête': 2,
          'servie': 3
        }
        if (statusOrder[a.status] !== statusOrder[b.status]) {
          return statusOrder[a.status] - statusOrder[b.status]
        }
        // Puis par heure
        return new Date(a.orderTime) - new Date(b.orderTime)
      })
    }
  },
  methods: {
    formatTime(timestamp) {
      return new Date(timestamp).toLocaleTimeString()
    }
  }
}
</script>

<style scoped>
.orders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.order-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background-color: #f8f9fa;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9em;
}

.status.en-préparation {
  background-color: #ffd700;
}

.status.prête {
  background-color: #90EE90;
}

.status.servie {
  background-color: #87CEEB;
}

.order-items ul {
  list-style: none;
  padding-left: 0;
}

.cancel-btn {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #cc0000;
}
</style>