<template>
  <NavBar />
  <div class="server-orders-list-view">
    <h2>Commandes en cours</h2>
    <OrderFilters
      :tables="uniqueTables"
      :model-value-table="selectedTable"
      :model-value-status="selectedStatus"
      @update:modelValueTable="selectedTable = $event"
      @update:modelValueStatus="selectedStatus = $event"
    />
    <div v-if="loading">Chargement...</div>
    <div v-else-if="filteredOrders.length === 0">Aucune commande en cours.</div>
    <OrderTable
      v-else
      :orders="filteredOrders"
      :sort-key="sortKey"
      :sort-asc="sortAsc"
      @sort="sortBy"
      @serve="markAsServed"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import apiClient from '@/services/apiClient';
import NavBar from '@/components/NavBar.vue';
import OrderFilters from '@/components/server/OrderFilters.vue';
import OrderTable from '@/components/server/OrderTable.vue';

const orders = ref([]);
const loading = ref(true);
const selectedTable = ref('');
const selectedStatus = ref('');
const sortKey = ref('');
const sortAsc = ref(true);
let intervalId = null;

const fetchOrders = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/server/orders/current');
    orders.value = response.data;
  } finally {
    loading.value = false;
  }
};

const markAsServed = async (orderId) => {
  await apiClient.patch(`/api/server/orders/${orderId}/status`, { status: 'servie' });
  await fetchOrders();
};

// Rafraîchissement automatique toutes les 10s
onMounted(() => {
  fetchOrders();
  intervalId = setInterval(fetchOrders, 10000);
});
onUnmounted(() => {
  clearInterval(intervalId);
});

// Tables uniques pour le filtre
const uniqueTables = computed(() => {
  return [...new Set(orders.value.map(o => o.tableNumber))];
});

// Filtrage et tri
const filteredOrders = computed(() => {
  let result = orders.value;
  if (selectedTable.value) {
    result = result.filter(o => o.tableNumber === Number(selectedTable.value));
  }
  if (selectedStatus.value) {
    result = result.filter(o => o.status === selectedStatus.value);
  }
  if (sortKey.value) {
    result = [...result].sort((a, b) => {
      if (a[sortKey.value] < b[sortKey.value]) return sortAsc.value ? -1 : 1;
      if (a[sortKey.value] > b[sortKey.value]) return sortAsc.value ? 1 : -1;
      return 0;
    });
  }
  return result;
});

function sortBy(key) {
  if (sortKey.value === key) {
    sortAsc.value = !sortAsc.value;
  } else {
    sortKey.value = key;
    sortAsc.value = true;
  }
}
</script>

<style scoped>
.server-orders-list-view {
  max-width: 900px;
  margin: 32px auto;
}
</style>