<template>
  <form @submit.prevent="submit">
    <div>
      <label>Nom de la recette :</label>
      <input v-model="form.name" required placeholder="Nom" />
    </div>
    <div>
      <label>Pour :</label>
      <input v-model.number="form.quantite" type="number" min="1" required placeholder="Quantité totale" />
      <input v-model="form.unite" required placeholder="Unité totale (ex: gramme, portion...)" />
    </div>
    <div>
      <label>Ingrédients :</label>
      <div v-for="(item, idx) in form.ingredients" :key="idx" style="margin-bottom: 8px;">
        <select v-model="item.ingredientId" required>
          <option value="" disabled>Choisir un ingrédient</option>
          <option v-for="ing in ingredients" :key="ing.id" :value="ing.id">{{ ing.name }}</option>
        </select>
        <input v-model.number="item.quantite" type="number" min="0" required placeholder="Quantité" style="width: 80px;" />
        <input v-model="item.unite" required placeholder="Unité" style="width: 80px;" />
        <button type="button" @click="removeIngredient(idx)">Supprimer</button>
      </div>
      <button type="button" class="add-btn" @click="addIngredient">Ajouter un ingrédient</button>
    </div>
    <button type="submit">{{ recipe ? 'Modifier' : 'Créer' }} la recette</button>
    <button type="button" @click="$emit('cancel')">Annuler</button>
  </form>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
const props = defineProps<{ ingredients: any[], recipe?: any }>()
const emit = defineEmits(['save', 'cancel'])

const form = ref({
  name: '',
  quantite: 1,
  unite: '',
  ingredients: [{ ingredientId: '', quantite: 0, unite: '' }]
})

watch(() => props.recipe, (val) => {
  if (val) {
    form.value = {
      name: val.name,
      quantite: val.quantite,
      unite: val.unite,
      ingredients: val.ingredients
        ? val.ingredients.map((i: any) => ({
            ingredientId: i.ingredientId,
            quantite: i.quantite,
            unite: i.unite
          }))
        : [{ ingredientId: '', quantite: 0, unite: '' }]
    }
  } else {
    form.value = {
      name: '',
      quantite: 1,
      unite: '',
      ingredients: [{ ingredientId: '', quantite: 0, unite: '' }]
    }
  }
}, { immediate: true })

function addIngredient() {
  form.value.ingredients.push({ ingredientId: '', quantite: 0, unite: '' })
}
function removeIngredient(idx: number) {
  form.value.ingredients.splice(idx, 1)
}
function submit() {
  emit('save', { ...form.value })
}
</script>

<style scoped>
form {
  margin-top: 24px;
  background: #f9f9f9;
  border-radius: 10px;
  padding: 24px 18px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06);
}
label {
  font-weight: 600;
  color: #34495e;
  margin-bottom: 6px;
  display: block;
}
input, select {
  margin: 6px 8px 12px 0;
  padding: 7px 10px;
  border: 1px solid #d0d0d0;
  border-radius: 5px;
  font-size: 1em;
}
input[type="number"] {
  width: 90px;
}
button[type="button"] {
  background: #e74c3c;
  color: #fff;
  border: none;
  margin-left: 8px;
  padding: 6px 12px;
  border-radius: 5px;
  cursor: pointer;
}
button[type="button"]:hover {
  background: #c0392b;
}
button[type="submit"] {
  background: #27ae60;
  color: #fff;
  border: none;
  margin-top: 12px;
  padding: 8px 18px;
  border-radius: 6px;
  cursor: pointer;
}
button[type="submit"]:hover {
  background: #219150;
}
button[type="button"].add-btn {
  background: #3498db;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 8px;
  margin-left: 0;
}
button[type="button"].add-btn:hover {
  background: #2980b9;
}
.edit-btn {
  background: #f39c12;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 8px;
}
.edit-btn:hover {
  background: #e67e22;
}
</style>

