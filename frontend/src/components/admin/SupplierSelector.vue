<template>
  <div class="supplier-selector">
    <label for="supplier-select">Choisir un fournisseur :</label>
    <select
      id="supplier-select"
      :value="modelValue"
      @change="$emit('update:modelValue', parseInt($event.target.value))"
      :disabled="suppliers.length === 0"
    >
      <option :value="null" disabled>-- Sélectionnez un fournisseur --</option>
      <option v-for="supplier in suppliers" :key="supplier.id" :value="supplier.id">
        {{ supplier.name }}
      </option>
    </select>
    <p v-if="suppliers.length === 0">Aucun fournisseur disponible.</p>
  </div>
</template>

<script setup>
defineProps({
  suppliers: {
    type: Array,
    required: true,
    default: () => []
  },
  modelValue: { // Utilisé pour v-model
    type: Number,
    default: null
  }
});

defineEmits(['update:modelValue']);
</script>

<style scoped>
.supplier-selector {
  margin-bottom: 20px;
}
.supplier-selector label {
  display: block;
  margin-bottom: 5px;
}
.supplier-selector select {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
</style>