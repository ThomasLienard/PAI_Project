<template>
  <div>
    <h1>Gestion des commandes</h1>
    <div>
      <h2>Commandes</h2>
      <ul>
        <li v-for="order in orders" :key="order.id">
          {{ order.dishName }} - Quantité: {{ order.quantity }} - Statut: {{ order.status }}
        </li>
      </ul>
    </div>
    <div>
      <h2>Niveaux de stock</h2>
      <ul>
        <li v-for="dish in dishes" :key="dish.id">
          {{ dish.name }} - Stock: {{ dish.stock }}
          <span v-if="dish.stock <= dish.alertThreshold" class="alert">Stock faible!</span>
        </li>
      </ul>
    </div>
    <div>
      <h2>Fournisseurs</h2>
      <form @submit.prevent="saveSupplier">
        <input v-model="currentSupplier.name" placeholder="Nom du fournisseur" required />
        <input v-model="currentSupplier.contact" placeholder="Contact" required />
        <button type="submit">{{ isEditingSupplier ? 'Modifier' : 'Ajouter' }} le fournisseur</button>
      </form>
      <ul>
        <li v-for="supplier in suppliers" :key="supplier.id">
          {{ supplier.name }} - {{ supplier.contact }}
          <button @click="editSupplier(supplier)">Modifier</button>
          <button @click="deleteSupplier(supplier.id)">Supprimer</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const orders = ref([])
const dishes = ref([])
const suppliers = ref([])
const currentSupplier = ref({ id: null, name: '', contact: '' })
const isEditingSupplier = ref(false)

const fetchOrders = async () => {
  const response = await axios.get('/api/orders')
  orders.value = response.data
}

const fetchDishes = async () => {
  const response = await axios.get('/api/menus')
  dishes.value = response.data
  checkStockLevels()
}

const fetchSuppliers = async () => {
  const response = await axios.get('/api/suppliers')
  suppliers.value = response.data
}

const saveSupplier = async () => {
  if (isEditingSupplier.value) {
    await axios.put(`/api/suppliers/${currentSupplier.value.id}`, currentSupplier.value)
  } else {
    const response = await axios.post('/api/suppliers', currentSupplier.value)
    suppliers.value.push(response.data)
  }
  resetSupplierForm()
  fetchSuppliers()
}

const editSupplier = (supplier) => {
  currentSupplier.value = { ...supplier }
  isEditingSupplier.value = true
}

const deleteSupplier = async (id) => {
  await axios.delete(`/api/suppliers/${id}`)
  fetchSuppliers()
}

const resetSupplierForm = () => {
  currentSupplier.value = { id: null, name: '', contact: '' }
  isEditingSupplier.value = false
}

const checkStockLevels = () => {
  dishes.value.forEach(dish => {
    if (dish.stock <= dish.alertThreshold) {
      alert(`Stock faible pour ${dish.name}!`)
      orderFromSupplier(dish)
    }
  })
}

const orderFromSupplier = async (dish) => {
  // Logique pour commander automatiquement aux fournisseurs
  const supplier = suppliers.value.find(s => s.id === dish.supplierId)
  if (supplier) {
    await axios.post('/api/orders', {
      dishId: dish.id,
      supplierId: supplier.id,
      quantity: dish.reorderQuantity
    })
    alert(`Commande automatique passée pour ${dish.name} auprès de ${supplier.name}`)
  }
}

fetchOrders()
fetchDishes()
fetchSuppliers()
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

input, button {
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

.alert {
  color: red;
  font-weight: bold;
}
</style>