<template>
  <ul>
    <li v-for="recipe in recipes" :key="recipe.id">
      <strong>{{ recipe.name }}</strong>
      <br>
      Pour : {{ recipe.quantite }} {{ recipe.unite }}
      <br>
      Ingrédients :
      <span v-if="recipe.ingredients && recipe.ingredients.length">
        <ul>
          <li v-for="(ing, idx) in recipe.ingredients" :key="ing.ingredientId">
            {{ ing.quantite }} {{ ing.unite }} de {{ ing.ingredientName }}
          </li>
        </ul>
      </span>
      <span v-else>Aucun ingrédient</span>
      <button @click="$emit('edit', recipe)">Modifier</button>
      <button @click="$emit('delete', recipe.id)">Supprimer</button>
    </li>
  </ul>
</template>

<script setup lang="ts">
defineProps<{ recipes: any[] }>()
defineEmits(['edit', 'delete'])
</script>