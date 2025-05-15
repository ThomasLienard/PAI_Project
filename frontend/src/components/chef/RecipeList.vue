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
      <div v-if="!recipe.disponible" class="indisponible">
        Indisponible
      </div>
      <button class="edit-btn" @click="$emit('edit', recipe)">Modifier</button>
      <button class="delete-btn" @click="$emit('delete', recipe.id)">Supprimer</button>
    </li>
  </ul>
</template>

<script setup lang="ts">
defineProps<{ recipes: any[] }>()
defineEmits(['edit', 'delete'])
</script>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}
li {
  background: #f4f8fb;
  border-radius: 8px;
  margin-bottom: 18px;
  padding: 18px 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
strong {
  font-size: 1.15em;
  color: #2d3a4b;
}
li ul {
  margin: 8px 0 0 0;
  padding-left: 18px;
}
.edit-btn {
  background: #f39c12; /* orange */
  color: #fff;
  border: none;
  margin-right: 8px;
  margin-top: 8px;
  padding: 6px 14px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
.edit-btn:hover {
  background: #d35400; /* orange foncé */
}
.delete-btn {
  background: #e74c3c; /* rouge */
  color: #fff;
  border: none;
  margin-right: 8px;
  margin-top: 8px;
  padding: 6px 14px;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 500;
  transition: background 0.2s;
}
.delete-btn:hover {
  background: #c0392b; /* rouge foncé */
}

.indisponible {
  color: #e74c3c; 
  font-size: 1.5em;
  font-weight: bold;
  margin-top: 10px;
}
</style>