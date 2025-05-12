<template>
  <div>
    <h3>Ingrédients</h3>
    <ul>
      <li v-for="ing in ingredients" :key="ing.id">
        <span>
          <img v-if="ing.photoUrl" :src="ing.photoUrl" alt="photo" width="24" height="24" style="vertical-align:middle;"/>
          {{ ing.name }} ({{ ing.unit }}) - Catégorie : {{ getCategoryName(ing.categoryId) }}
        </span>
        <button @click="editIngredient(ing)">✏️</button>
        <button @click="removeIngredient(ing.id)">🗑️</button>
      </li>
    </ul>
    <!-- Formulaire d'ajout -->
    <form v-if="!editingIngredient" @submit.prevent="addIngredient" enctype="multipart/form-data">
      <div>
        <label>Nom :</label><br>
        <input v-model="newIngredient.name" required />
      </div>
      <div>
        <label>Unité de mesure :</label><br>
        <input v-model="newIngredient.unit" required />
      </div>
      <div>
        <label>Description :</label><br>
        <input v-model="newIngredient.description" />
      </div>
      <div>
        <label>Photo :</label><br>
        <input type="file" @change="onFileChange" accept="image/*" />
      </div>
      <div>
        <label>Stock initial :</label><br>
        <input v-model.number="newIngredient.initialStock" type="number" />
      </div>
      <div>
        <label>Seuil d'alerte :</label><br>
        <input v-model.number="newIngredient.alertThreshold" type="number" />
      </div>
      <div>
        <label>Quantité recommandée à commander :</label><br>
        <input v-model.number="newIngredient.recommendedOrderQty" type="number" />
      </div>
      <div>
        <label>Durée de conservation (jours) :</label><br>
        <input v-model.number="newIngredient.shelfLifeDays" type="number" />
      </div>
      <div>
        <label>Catégorie :</label><br>
        <select v-model="newIngredient.categoryId" required>
          <option value="" disabled>Choisir une catégorie</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">
            {{ cat.name }}
          </option>
        </select>
      </div>
      <button type="submit">Ajouter</button>
    </form>
    <!-- Formulaire d'édition -->
    <form v-else @submit.prevent="updateIngredient" enctype="multipart/form-data">
      <div>
        <label>Nom :</label><br>
        <input v-model="editingIngredient.name" required />
      </div>
      <div>
        <label>Unité de mesure :</label><br>
        <input v-model="editingIngredient.unit" required />
      </div>
      <div>
        <label>Description :</label><br>
        <input v-model="editingIngredient.description" />
      </div>
      <div>
        <label>Photo :</label><br>
        <input type="file" @change="onFileChange" accept="image/*" />
      </div>
      <div>
        <label>Stock initial :</label><br>
        <input v-model.number="editingIngredient.initialStock" type="number" />
      </div>
      <div>
        <label>Seuil d'alerte :</label><br>
        <input v-model.number="editingIngredient.alertThreshold" type="number" />
      </div>
      <div>
        <label>Quantité recommandée à commander :</label><br>
        <input v-model.number="editingIngredient.recommendedOrderQty" type="number" />
      </div>
      <div>
        <label>Durée de conservation (jours) :</label><br>
        <input v-model.number="editingIngredient.shelfLifeDays" type="number" />
      </div>
      <div>
        <label>Catégorie :</label><br>
        <select v-model="editingIngredient.categoryId" required>
          <option value="" disabled>Choisir une catégorie</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">
            {{ cat.name }}
          </option>
        </select>
      </div>
      <button type="submit">Valider</button>
      <button type="button" @click="cancelEdit">Annuler</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAllIngredients, createIngredient, deleteIngredient, getAllCategories, updateIngredient as updateIngredientApi } from '../../services/apiClient'

const emit = defineEmits(['refresh'])

const ingredients = ref([])
const categories = ref([])
const newIngredient = ref({
  name: '', unit: '', description: '', initialStock: 0,
  alertThreshold: 0, recommendedOrderQty: 0, shelfLifeDays: 0, categoryId: ''
})
const photoFile = ref(null)
const editingIngredient = ref(null)

const fetchIngredients = async () => {
  ingredients.value = await getAllIngredients()
}

const fetchCategories = async () => {
  categories.value = await getAllCategories()
}

const onFileChange = (e) => {
  photoFile.value = e.target.files[0]
}

const addIngredient = async () => {
  const formData = new FormData()
  formData.append('name', newIngredient.value.name)
  formData.append('unit', newIngredient.value.unit)
  formData.append('description', newIngredient.value.description)
  formData.append('initialStock', newIngredient.value.initialStock)
  formData.append('alertThreshold', newIngredient.value.alertThreshold)
  formData.append('recommendedOrderQty', newIngredient.value.recommendedOrderQty)
  formData.append('shelfLifeDays', newIngredient.value.shelfLifeDays)
  formData.append('categoryId', newIngredient.value.categoryId)
  if (photoFile.value) {
    formData.append('photo', photoFile.value)
  }
  await createIngredient(formData)
  newIngredient.value = {
    name: '', unit: '', description: '', initialStock: 0,
    alertThreshold: 0, recommendedOrderQty: 0, shelfLifeDays: 0, categoryId: ''
  }
  photoFile.value = null
  await fetchIngredients()
  emit('refresh')
}

const removeIngredient = async (id) => {
  await deleteIngredient(id)
  await fetchIngredients()
  emit('refresh')
}

const editIngredient = (ing) => {
  editingIngredient.value = { ...ing }
  photoFile.value = null
}

const updateIngredient = async () => {
  const formData = new FormData()
  formData.append('name', editingIngredient.value.name)
  formData.append('unit', editingIngredient.value.unit)
  formData.append('description', editingIngredient.value.description)
  formData.append('initialStock', editingIngredient.value.initialStock)
  formData.append('alertThreshold', editingIngredient.value.alertThreshold)
  formData.append('recommendedOrderQty', editingIngredient.value.recommendedOrderQty)
  formData.append('shelfLifeDays', editingIngredient.value.shelfLifeDays)
  formData.append('categoryId', editingIngredient.value.categoryId)
  if (photoFile.value) {
    formData.append('photo', photoFile.value)
  }
  await updateIngredientApi(editingIngredient.value.id, formData)
  editingIngredient.value = null
  photoFile.value = null
  await fetchIngredients()
  emit('refresh')
}

const cancelEdit = () => {
  editingIngredient.value = null
  photoFile.value = null
}

const getCategoryName = (id) => {
  const cat = categories.value.find(c => c.id === id)
  return cat ? cat.name : 'Inconnue'
}

onMounted(() => {
  fetchIngredients()
  fetchCategories()
})
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

form input[type="file"] {
  padding: 0.2rem 0;
}

form button[type="submit"] {
  margin-top: 1rem;
  padding: 0.6rem 1.5rem;
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
  padding-left: 0;
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