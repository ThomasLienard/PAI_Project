<template>
  <table>
    <thead>
      <tr>
        <th>Photo</th>
        <th>Nom</th>
        <th>Description</th>
        <th>Prix</th>
        <th>Catégorie</th>
        <th>Tags</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="dish in dishes" :key="dish.id">
        <td>
          <img
            v-if="dish.imageUrl"
            :src="getImageUrl(dish.imageUrl)"
            alt="photo"
            style="max-width: 80px; max-height: 80px; object-fit: cover;"
          />
        </td>
        <td>{{ dish.name }}</td>
        <td>{{ dish.description }}</td>
        <td>{{ dish.price }} €</td>
        <td>{{ dish.categoryName || dish.category?.name }}</td>
        <td>
          <span v-for="tag in dish.tags" :key="tag.id">{{ tag.name }} </span>
        </td>
        <td>
          <button @click="$emit('edit', dish)">Modifier</button>
          <button @click="$emit('delete', dish.id)">Supprimer</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
const props = defineProps({
  dishes: { type: Array, default: () => [] }
})

function getImageUrl(url) {
  if (!url) return ''
  return url.startsWith('http') ? url : `http://localhost:8080${url}`
}
</script>

<style scoped>
.tag {
  background: #eee;
  border-radius: 4px;
  padding: 2px 6px;
  margin-left: 4px;
  font-size: 0.9em;
}
</style>