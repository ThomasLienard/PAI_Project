<template>
  <div>
    <NavBar />
    <div class="server-orders">
      <div class="header">
        <h1>Commandes en cours</h1>
        <router-link to="/server" class="back-btn">
          <i class="fas fa-arrow-left"></i> Retour
        </router-link>
      </div>
      
      <div v-if="loading" class="loading">
        Chargement des commandes...
      </div>
      
      <div v-else>
        <div v-if="currentOrders && currentOrders.length > 0">
          <CurrentOrders 
            :orders="currentOrders"
            @cancelOrder="handleCancelOrder"
          />
        </div>
        <div v-else class="empty-state">
          <i class="fas fa-clipboard-list empty-icon"></i>
          <p>Aucune commande en cours</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import CurrentOrders from '@/components/server/CurrentOrders.vue'

export default {
  name: 'ServerOrdersView',
  components: { 
    NavBar,
    CurrentOrders 
  },
  data() {
    return {
      currentOrders: [],
      loading: true
    }
  },
  async created() {
    try {
      await this.fetchOrders()
    } catch (error) {
      console.error('Erreur lors du chargement des commandes:', error)
    } finally {
      this.loading = false
    }
  },
  methods: {
    async fetchOrders() {
      try {
        const response = await this.$axios.get('/api/orders/current')
        this.currentOrders = response.data || []
      } catch (error) {
        console.error('Erreur:', error)
        this.currentOrders = []
      }
    },
    async handleCancelOrder(orderId) {
      try {
        await this.$axios.delete(`/api/orders/${orderId}`)
        await this.fetchOrders()
      } catch (error) {
        console.error('Erreur lors de l\'annulation:', error)
      }
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.back-btn {
  text-decoration: none;
  color: #42b983;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 1px solid #42b983;
  border-radius: 4px;
  transition: all 0.3s;
}

.back-btn:hover {
  background-color: #42b983;
  color: white;
}

h1 {
  color: #2c3e50;
  margin: 0;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  background: #f8f9fa;
  border-radius: 8px;
  margin-top: 2rem;
}

.empty-icon {
  font-size: 4rem;
  color: #ddd;
  margin-bottom: 1rem;
}

.empty-state p {
  color: #666;
  font-size: 1.2rem;
}
</style>