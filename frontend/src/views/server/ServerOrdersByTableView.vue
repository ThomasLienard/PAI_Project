<template>
  <NavBar />  
  <div class="orders-by-table-view">
    <h2>Commandes par table</h2>
    <TableSelection @select-table="handleTableSelect" :required="false" />

    <div v-if="selectedTable" class="table-orders">
      <h3>Commandes pour la table {{ selectedTable.numero }}</h3>
      <div v-if="loading">Chargement...</div>
      <div v-else-if="orders.length === 0">Aucune commande pour cette table.</div>
      <ul v-else>
        <li v-for="order in orders" :key="order.id">
          Commande #{{ order.id }} – Statut : <b>{{ order.status }}</b>
          <ul>
            <li v-for="item in order.items" :key="item.id">
              {{ item.name }} x{{ item.quantity }}
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import TableSelection from '@/components/server/TableSelection.vue';
import apiClient from '@/services/apiClient';

const selectedTable = ref(null);
const orders = ref([]);
const loading = ref(false);

const handleTableSelect = async (table) => {
  selectedTable.value = table;
  loading.value = true;
  try {
    const response = await apiClient.get(`/server/orders/tables/${table.id}`);
    orders.value = response.data;
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.orders-by-table-view {
  max-width: 800px;
  margin: 32px auto;
}
.table-orders {
  margin-top: 24px;
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 8px;
}
</style>