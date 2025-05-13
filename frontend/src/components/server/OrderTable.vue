<template>
  <table class="orders-table">
    <thead>
      <tr>
        <th @click="sort('tableNumber')">Table</th>
        <th @click="sort('id')">Commande #</th>
        <th @click="sort('status')">Statut</th>
        <th>Plats</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="order in orders" :key="order.id">
        <td>{{ order.tableNumber }}</td>
        <td>{{ order.id }}</td>
        <td>
          <OrderStatusBadge :status="order.status" />
        </td>
        <td>
          <ul>
            <li v-for="item in order.items" :key="item.id">
              {{ item.name }} x{{ item.quantity }}
            </li>
          </ul>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
import OrderStatusBadge from './OrderStatusBadge.vue';
const props = defineProps({
  orders: Array,
  sortKey: String,
  sortAsc: Boolean
});
const emit = defineEmits(['sort', 'serve']);
function sort(key) {
  emit('sort', key);
}
</script>

<style scoped>
.orders-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 24px;
}
.orders-table th, .orders-table td {
  border: 1px solid #eee;
  padding: 8px 12px;
  text-align: left;
  cursor: pointer;
}
.serve-btn {
  background: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 6px 12px;
  cursor: pointer;
}
.serve-btn:disabled {
  background: #ccc;
  color: #666;
  cursor: not-allowed;
}
.serve-btn:hover:enabled {
  background: #0056b3;
}
</style>