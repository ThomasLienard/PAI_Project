<template>
  <div>
    <h1>Gestion des menus</h1>
    <form @submit.prevent="saveDish">
      <input v-model="currentDish.name" placeholder="Nom du plat" required />
      <input v-model="currentDish.price" type="number" placeholder="Prix" required />
      <input v-model="currentDish.allergens" placeholder="Allergènes" />
      <select v-model="currentDish.category" required>
        <option value="entrée">Entrée</option>
        <option value="plat">Plat</option>
        <option value="dessert">Dessert</option>
        <option value="boisson">Boisson</option>
      </select>
      <input v-model="currentDish.stock" type="number" placeholder="Stock" required />
      <button type="submit">{{ isEditing ? 'Modifier' : 'Ajouter' }} le plat</button>
    </form>
    <ul>
      <li v-for="dish in dishes" :key="dish.id">
        {{ dish.name }} - {{ dish.price }}€ - {{ dish.category }} - Stock: {{ dish.stock }}
        <button @click="editDish(dish)">Modifier</button>
        <button @click="deleteDish(dish.id)">Supprimer</button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const dishes = ref([])
const currentDish = ref({ id: null, name: '', price: 0, allergens: '', category: 'entrée', stock: 0 })
const isEditing = ref(false)

const fetchDishes = async () => {
  const response = await axios.get('/api/menus')
  dishes.value = response.data
}

const saveDish = async () => {
  if (isEditing.value) {
    await axios.put(`/api/menus/${currentDish.value.id}`, currentDish.value)
  } else {
    const response = await axios.post('/api/menus', currentDish.value)
    dishes.value.push(response.data)
  }
  resetForm()
  fetchDishes()
}

const editDish = (dish) => {
  currentDish.value = { ...dish }
  isEditing.value = true
}

const deleteDish = async (id) => {
  await axios.delete(`/api/menus/${id}`)
  fetchDishes()
}

const resetForm = () => {
  currentDish.value = { id: null, name: '', price: 0, allergens: '', category: 'entrée', stock: 0 }
  isEditing.value = false
}

fetchDishes()
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

input, select, button {
  padding: 0.5rem;
  font-size: 1rem;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid #ccc;
}

button {
  margin-left: 1rem;
}
</style>