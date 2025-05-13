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
      <button type="button" @click="addIngredient">Ajouter un ingrédient</button>
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

// Ce composant est un formulaire utilisé pour ajouter ou modifier une recette.
// Rôle :
// - Permettre au cuisinier de saisir les informations d'une recette (nom, ingrédients, étapes).
// - Valider les données saisies avant de les envoyer au backend.