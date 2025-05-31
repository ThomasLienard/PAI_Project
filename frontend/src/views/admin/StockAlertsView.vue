<!-- filepath: frontend/src/views/admin/StockAlertsView.vue -->
<template>
  <div>
    <NavBar />
    <h1>Alertes de stock</h1>
    <ul>
      <li v-for="ingredient in criticalIngredients" :key="ingredient.id">
        {{ ingredient.name }} - Stock : {{ ingredient.initialStock }}
        <span style="color: red;">⚠️ Stock critique</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getAllIngredients } from '../../services/apiClient';

const criticalIngredients = ref([]);

const fetchCriticalIngredients = async () => {
  const ingredients = await getAllIngredients();
  criticalIngredients.value = ingredients.filter(ing => ing.initialStock <= ing.alertThreshold);
};

onMounted(fetchCriticalIngredients);
</script>