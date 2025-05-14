<template>
  <form @submit.prevent="onSubmit">
    <input v-model="localDish.name" placeholder="Nom du plat" required />
    <input
      v-model.number="localDish.price"
      type="number"
      placeholder="Prix (€)"
      required
      min="0"
      step="0.01"
    />
    <textarea v-model="localDish.description" placeholder="Description" required />
    <select v-model="localDish.categoryId" required>
      <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
    </select>
    <input type="file" @change="onFileChange" accept="image/png, image/jpeg" />
    <div>
      <label v-for="tag in tags" :key="tag.id">
        <input type="checkbox" :value="tag.id" v-model="localDish.tagIds" />
        {{ tag.name }}
      </label>
    </div>
    <button type="submit">{{ isEdit ? 'Modifier' : 'Ajouter' }}</button>
    <button type="button" @click="$emit('cancel')">Annuler</button>
  </form>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  dish: {
    type: Object,
    default: () => ({
      id: null,
      name: '',
      description: '',
      price: 0,
      imageUrl: '',
      categoryId: null,
      tagIds: []
    })
  },
  categories: { type: Array, default: () => [] },
  tags: { type: Array, default: () => [] },
  isEdit: Boolean
})

const emit = defineEmits(['submit', 'cancel'])

const localDish = ref({ ...props.dish })

watch(() => props.dish, (val) => {
  localDish.value = { ...val }
})

function onFileChange(e) {
  localDish.value.photo = e.target.files[0]
}

function onSubmit() {
  emit('submit', { ...localDish.value }, localDish.value.photo)
}
</script>