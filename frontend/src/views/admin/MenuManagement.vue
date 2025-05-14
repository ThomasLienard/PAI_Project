<template>
  <div>
    <NavBar />
    <h1>Gestion des plats</h1>
    <DishForm
      v-if="showForm"
      :dish="currentDish"
      :categories="categories"
      :tags="tags"
      :isEdit="isEditing"
      @submit="saveDish"
      @cancel="closeForm"
    />
    <button v-if="!showForm" @click="openAddForm">Ajouter un plat</button>
    <DishList
      :dishes="dishes"
      @edit="editDish"
      @delete="deleteDish"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import NavBar from '../../components/NavBar.vue';
import DishForm from '../../components/admin/DishForm.vue'
import DishList from '../../components/admin/DishList.vue'
import apiClient, { getAllCategories } from '../../services/apiClient'

const dishes = ref([])
const categories = ref([])
const tags = ref([])
const showForm = ref(false)
const isEditing = ref(false)
const currentDish = ref({
  id: null,
  name: '',
  description: '',
  price: 0,
  imageUrl: '',
  categoryId: null,
  tagIds: []
})

async function fetchDishes() {
  const res = await apiClient.get('/admin/dishes')
  dishes.value = res.data
}
async function fetchCategories() {
  const res = await apiClient.get('/admin/dishes/categories')
  categories.value = res.data
}
async function fetchTags() {
  try {
    const res = await apiClient.get('/tags')
    tags.value = res.data
  } catch (e) {
    tags.value = []
    console.error('Erreur lors du chargement des tags :', e)
  }
}

function openAddForm() {
  isEditing.value = false
  currentDish.value = {
    id: null,
    name: '',
    description: '',
    price: 0,
    imageUrl: '',
    categoryId: null,
    tagIds: []
  }
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

function editDish(dish) {
  isEditing.value = true
  currentDish.value = { ...dish }
  showForm.value = true
}

async function saveDish(dish, photo) {
  const formData = new FormData()
  formData.append('dish', new Blob([JSON.stringify(dish)], { type: 'application/json' }))
  if (photo) formData.append('photo', photo)
  if (isEditing.value) {
    await apiClient.put(`/admin/dishes/${dish.id}`, formData)
  } else {
    await apiClient.post('/admin/dishes', formData)
  }
  await fetchDishes()
  closeForm()
}

async function deleteDish(id) {
  if (confirm('Supprimer ce plat ?')) {
    await apiClient.delete(`/admin/dishes/${id}`)
    await fetchDishes()
  }
}

onMounted(() => {
  fetchDishes()
  fetchCategories()
  fetchTags()
})
</script>