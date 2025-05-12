<template>
  <div>
    <h3>Catégories d'ingrédients</h3>
    <ul>
      <li v-for="cat in categories" :key="cat.id">
        <span>
          <img v-if="cat.icon" :src="cat.icon" alt="icon" width="24" height="24" style="vertical-align:middle;"/>
          {{ cat.name }}
        </span>
        <button @click="editCategory(cat)">✏️</button>
        <button @click="deleteCategory(cat.id)">🗑️</button>
      </li>
    </ul>
    <!-- Formulaire d'ajout -->
    <form v-if="!editingCategory" @submit.prevent="addCategory" enctype="multipart/form-data">
      <div>
        <label>Nom de la catégorie :</label><br>
        <input v-model="newCategory.name" required />
      </div>
      <div>
        <label>Icône :</label><br>
        <input type="file" @change="onFileChange" accept="image/*" />
      </div>
      <button type="submit">Ajouter</button>
    </form>
    <!-- Formulaire d'édition -->
    <form v-else @submit.prevent="updateCategory" enctype="multipart/form-data">
      <div>
        <label>Nom de la catégorie :</label><br>
        <input v-model="editingCategory.name" required />
      </div>
      <div>
        <label>Icône :</label><br>
        <input type="file" @change="onFileChange" accept="image/*" />
      </div>
      <button type="submit">Valider</button>
      <button type="button" @click="cancelEdit">Annuler</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllCategories, createCategory, deleteCategory as apiDeleteCategory, updateCategory as apiUpdateCategory } from '../../services/apiClient'

const emit = defineEmits(['refresh'])

const categories = ref([])
const newCategory = ref({ name: '' })
const iconFile = ref(null)
const editingCategory = ref(null)

const fetchCategories = async () => {
  categories.value = await getAllCategories()
}

const onFileChange = (e) => {
  iconFile.value = e.target.files[0]
}

const addCategory = async () => {
  const formData = new FormData()
  formData.append('name', newCategory.value.name)
  if (iconFile.value) {
    formData.append('icon', iconFile.value)
  }
  await createCategory(formData)
  newCategory.value = { name: '' }
  iconFile.value = null
  await fetchCategories()
  emit('refresh')
}

const deleteCategory = async (id) => {
  await apiDeleteCategory(id)
  await fetchCategories()
  emit('refresh')
}

const editCategory = (cat) => {
  editingCategory.value = { ...cat }
  iconFile.value = null
}

const updateCategory = async () => {
  const formData = new FormData()
  formData.append('name', editingCategory.value.name)
  if (iconFile.value) {
    formData.append('icon', iconFile.value)
  }
  await apiUpdateCategory(editingCategory.value.id, formData)
  editingCategory.value = null
  iconFile.value = null
  await fetchCategories()
  emit('refresh')
}

const cancelEdit = () => {
  editingCategory.value = null
  iconFile.value = null
}

onMounted(fetchCategories)
</script>

<style scoped>
form > div {
  margin-bottom: 0;
}

form label {
  display: block;
  margin-bottom: 0;
  font-weight: 500;
}

form input,
form select {
  width: 100%;
  padding: 0.2rem 0.3rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-top: 0;
  box-sizing: border-box;
  font-size: 1rem;
}

form button[type="submit"] {
  margin-top: 1rem;
  padding: 0.5rem 1.5rem;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}

form button[type="submit"]:hover {
  background: #125ea6;
}

ul {
  margin-bottom: 2rem;
}

li {
  margin-bottom: 0.5rem;
  list-style: none;
  padding: 0.3rem 0.5rem;
  background: #f8f8f8;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

button {
  margin-left: 0.5rem;
}
</style>