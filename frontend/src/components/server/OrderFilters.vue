<template>
  <div class="filters">
    <label>
      Table :
      <select v-model="modelTable">
        <option value="">Toutes</option>
        <option
          v-for="table in tables"
          :key="table"
          :value="table"
        >
          {{ table }}
        </option>
      </select>
    </label>
    <label>
      Statut :
      <select v-model="modelStatus">
        <option value="">Tous</option>
        <option value="en_attente">En attente</option>
        <option value="en_preparation">En préparation</option>
        <option value="prete">Prête</option>
        <option value="servie">Servie</option>
      </select>
    </label>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  tables: {
    type: Array,
    default: () => []
  },
  modelValueTable: [String, Number],
  modelValueStatus: String
});
const emit = defineEmits(['update:modelValueTable', 'update:modelValueStatus']);

const modelTable = computed({
  get: () => props.modelValueTable,
  set: v => emit('update:modelValueTable', v)
});
const modelStatus = computed({
  get: () => props.modelValueStatus,
  set: v => emit('update:modelValueStatus', v)
});
</script>

<style scoped>
.filters {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
  align-items: center;
}
</style>