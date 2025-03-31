<template>
  <div>
    <NavBar />
    <div class="server-new-order">
      <div class="header">
        <h1>Nouvelle Commande</h1>
        <router-link to="/server" class="back-btn">
          <i class="fas fa-arrow-left"></i> Retour
        </router-link>
      </div>

      <NewOrder 
        @orderSubmitted="handleNewOrder"
      />

      <div v-if="showConfirmation" class="confirmation-message">
        {{ confirmationMessage }}
      </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import NewOrder from '@/components/server/NewOrder.vue'

export default {
  name: 'ServerNewOrderView',
  components: {
    NavBar,
    NewOrder
  }
  // ... Copiez la logique de nouvelle commande de l'ancien ServerView
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
</style>